package com.ocsentinel.controller;

import com.ocsentinel.model.OCUpdate;
import com.ocsentinel.service.RestPollingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST API Controller for Frontend Polling
 * 
 * This controller provides REST endpoints that the frontend can poll directly
 * as an alternative to WebSocket communication.
 * This is useful if WebSocket connections are problematic in some environments.
 */
@RestController
@RequestMapping("/api/rest")
@CrossOrigin(origins = "*")
public class RestPollingController {

    @Autowired
    private RestPollingService restPollingService;

    /**
     * Get latest option chain data via REST polling
     * Frontend can call this every 2 seconds instead of using WebSocket
     */
    @GetMapping("/latest-data")
    public ResponseEntity<OCUpdate> getLatestData() {
        OCUpdate latest = restPollingService.getLatestOC();
        if (latest == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(latest);
    }

    /**
     * Check if REST polling is active
     */
    @GetMapping("/polling-status")
    public ResponseEntity<PollingStatus> getPollingStatus() {
        PollingStatus status = new PollingStatus();
        status.setRunning(restPollingService.isRunning());
        status.setInstrument(restPollingService.getCurrentInstrument());
        status.setExpiry(restPollingService.getCurrentExpiry());
        status.setTimestamp(System.currentTimeMillis());
        return ResponseEntity.ok(status);
    }

    /**
     * Simple health check for REST endpoints
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "REST Polling API",
            "timestamp", System.currentTimeMillis()
        ));
    }

    // DTO for polling status
    public static class PollingStatus {
        private boolean running;
        private String instrument;
        private String expiry;
        private long timestamp;

        // Getters and setters
        public boolean isRunning() { return running; }
        public void setRunning(boolean running) { this.running = running; }
        public String getInstrument() { return instrument; }
        public void setInstrument(String instrument) { this.instrument = instrument; }
        public String getExpiry() { return expiry; }
        public void setExpiry(String expiry) { this.expiry = expiry; }
        public long getTimestamp() { return timestamp; }
        public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    }
}
