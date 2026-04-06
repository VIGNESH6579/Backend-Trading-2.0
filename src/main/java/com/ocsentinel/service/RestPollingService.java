package com.ocsentinel.service;

import com.ocsentinel.model.OCUpdate;
import com.ocsentinel.model.StatusMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * REST Polling Service - Replaces WebSocket V2 for Render compatibility
 * 
 * This service polls Angel One REST APIs every 2 seconds to get:
 * - Market data (prices, bid/ask)
 * - Option chain updates
 * - Positions/holdings if needed
 * 
 * Pros:
 * - Works on any server (no WebSocket restrictions)
 * - No "network unreachable" errors
 * - Easy to implement and debug
 * 
 * Cons:
 * - 1-2 second delay (not truly real-time)
 * - Higher API request frequency
 * - Slightly more backend load
 */
@Service
public class RestPollingService {

    private static final Logger log = LoggerFactory.getLogger(RestPollingService.class);

    @Autowired private AngelOneService angelService;
    @Autowired private BroadcastService broadcaster;

    // Polling configuration
    private static final int POLL_INTERVAL_SECONDS = 2; // Poll every 2 seconds
    
    // Current subscription state
    private final AtomicBoolean running = new AtomicBoolean(false);
    private String currentInstrument;
    private String currentExpiry;
    private String currentApiKey;

    // Latest data cache
    private volatile OCUpdate latestOC = null;
    private final ConcurrentHashMap<String, Object> cache = new ConcurrentHashMap<>();

    // ── START POLLING ─────────────────────────────────────────────────────────
    public void start(String instrument, String expiry, String apiKey) {
        this.currentInstrument = instrument;
        this.currentExpiry = expiry;
        this.currentApiKey = apiKey;
        running.set(true);

        log.info("Starting REST polling for {} {} (interval: {}s)", instrument, expiry, POLL_INTERVAL_SECONDS);
        
        // Load initial data immediately
        fetchInitialData();
        
        broadcaster.status(new StatusMessage("POLLING_ACTIVE", 
            "REST polling started for " + instrument + " " + expiry));
    }

    // ── STOP POLLING ──────────────────────────────────────────────────────────
    public void stop() {
        running.set(false);
        cache.clear();
        log.info("REST polling stopped");
        broadcaster.status(new StatusMessage("POLLING_STOPPED", "REST polling stopped"));
    }

    // ── SCHEDULED POLLING ─────────────────────────────────────────────────────
    @Scheduled(fixedRate = 2000) // Poll every 2 seconds
    public void pollMarketData() {
        if (!running.get()) return;
        
        try {
            // Fetch latest option chain data
            OCUpdate oc = angelService.fetchOptionChain(currentInstrument, currentExpiry, currentApiKey);
            
            if (oc != null) {
                // Update cache and broadcast
                latestOC = oc;
                
                // Add polling metadata
                oc.setDataSource("REST_POLLING");
                oc.setTimestamp(System.currentTimeMillis());
                
                // Broadcast to frontend
                broadcaster.ocUpdate(oc);
                
                List<OCUpdate.StrikeRow> strikes = oc.getStrikes();
                log.debug("Polled data: {} strikes, spot={}", 
                    strikes != null ? strikes.size() : 0, oc.getSpot());
            }
            
        } catch (Exception e) {
            log.error("REST polling error: {}", e.getMessage());
            broadcaster.status(new StatusMessage("POLLING_ERROR", 
                "Polling error: " + e.getMessage()));
        }
    }

    // ── INITIAL DATA LOAD ───────────────────────────────────────────────────────
    private void fetchInitialData() {
        try {
            OCUpdate oc = angelService.fetchOptionChain(currentInstrument, currentExpiry, currentApiKey);
            if (oc != null) {
                latestOC = oc;
                oc.setDataSource("REST_INITIAL");
                broadcaster.ocUpdate(oc);
                
                List<OCUpdate.StrikeRow> strikes = oc.getStrikes();
                log.info("Initial data loaded: {} strikes", strikes != null ? strikes.size() : 0);
            }
        } catch (Exception e) {
            log.error("Initial data load failed: {}", e.getMessage());
            broadcaster.status(new StatusMessage("INITIAL_LOAD_ERROR", 
                "Failed to load initial data: " + e.getMessage()));
        }
    }

    // ── GETTERS ───────────────────────────────────────────────────────────────
    public boolean isRunning() { 
        return running.get(); 
    }
    
    public OCUpdate getLatestOC() { 
        return latestOC; 
    }
    
    public String getCurrentInstrument() { 
        return currentInstrument; 
    }
    
    public String getCurrentExpiry() { 
        return currentExpiry; 
    }

    // ── CACHE MANAGEMENT ───────────────────────────────────────────────────────
    public void cacheData(String key, Object value) {
        cache.put(key, value);
    }
    
    public Object getCachedData(String key) {
        return cache.get(key);
    }
    
    public void clearCache() {
        cache.clear();
    }
}
