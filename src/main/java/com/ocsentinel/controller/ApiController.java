package com.ocsentinel.controller;

import com.ocsentinel.model.OCUpdate;
import com.ocsentinel.model.StatusMessage;
import com.ocsentinel.service.AngelOneService;
import com.ocsentinel.service.RestPollingService;
import com.ocsentinel.service.BroadcastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class ApiController {

    @Autowired private AngelOneService     angelService;
    @Autowired private RestPollingService  restPollingService;
    @Autowired private BroadcastService    broadcaster;
    @Autowired private com.ocsentinel.service.AiService aiService;

    // ── HEALTH ────────────────────────────────────────────────────────────────
    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();
        
        try {
            log.info("Health endpoint called");
            
            // Basic application info
            health.put("status", "UP");
            health.put("service", "OC Sentinel — REST Polling Edition");
            health.put("version", "2.1.0");
            health.put("timestamp", System.currentTimeMillis());
            
            // System information
            Runtime runtime = Runtime.getRuntime();
            health.put("memory", Map.of(
                "totalMB", runtime.totalMemory() / 1024 / 1024,
                "freeMB", runtime.freeMemory() / 1024 / 1024,
                "usedMB", (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024
            ));
            
            // Service status
            var session = angelService.getSession();
            health.put("services", Map.of(
                "angelOneService", session != null ? session.isLoggedIn() : false,
                "restPolling", restPollingService.isRunning(),
                "currentInstrument", restPollingService.getCurrentInstrument(),
                "currentExpiry", restPollingService.getCurrentExpiry()
            ));
            
            log.info("Health check successful");
            return ResponseEntity.ok(health);
            
        } catch (Exception e) {
            log.error("Health endpoint error: ", e);
            health.put("status", "ERROR");
            health.put("error", e.getMessage());
            health.put("timestamp", System.currentTimeMillis());
            return ResponseEntity.status(500).body(health);
        }
    }

    // ── LOGIN ─────────────────────────────────────────────────────────────────
    // POST /api/login
    // { "clientCode":"A123", "mpin":"1234", "totp":"123456", "apiKey":"xxx" }
    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(
            @RequestBody Map<String, String> body) {

        String clientCode = body.getOrDefault("clientCode", "").trim().toUpperCase();
        String mpin       = body.getOrDefault("mpin",       "").trim();
        String totp       = body.getOrDefault("totp",       "").trim();
        String apiKey     = body.getOrDefault("apiKey",     "").trim();

        if (clientCode.isEmpty() || mpin.isEmpty() || totp.isEmpty() || apiKey.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of(
                "success", false, "message", "All fields required"));
        }

        Map<String, Object> result = angelService.login(clientCode, mpin, totp, apiKey);
        return ResponseEntity.ok(result);
    }

    // ── LOGOUT ────────────────────────────────────────────────────────────────
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout() {
        restPollingService.stop();
        angelService.clearSession();
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ── EXPIRIES ──────────────────────────────────────────────────────────────
    // GET /api/expiries?instrument=NIFTY&apiKey=xxx
    @GetMapping("/expiries")
    public ResponseEntity<Map<String, Object>> expiries(
            @RequestParam String instrument,
            @RequestParam String apiKey) {

        List<String> exps = angelService.fetchExpiries(instrument, apiKey);
        return ResponseEntity.ok(Map.of("success", true, "expiries", exps));
    }

    // ── START REST POLLING FEED ─────────────────────────────────────────────────
    // POST /api/start-feed
    // { "instrument":"NIFTY", "expiry":"27Mar2025", "apiKey":"xxx" }
    @PostMapping("/start-feed")
    public ResponseEntity<Map<String, Object>> startFeed(
            @RequestBody Map<String, String> body) {

        if (!angelService.getSession().isLoggedIn()) {
            return ResponseEntity.ok(Map.of(
                "success", false, "message", "Not logged in"));
        }

        String instrument = body.getOrDefault("instrument", "NIFTY");
        String expiry     = body.getOrDefault("expiry",     "");
        String apiKey     = body.getOrDefault("apiKey",     "");

        // Stop any existing feed
        restPollingService.stop();

        // Start fresh with REST polling
        restPollingService.start(instrument, expiry, apiKey);

        broadcaster.status(new StatusMessage("STARTING",
            "Starting REST polling feed for " + instrument + " " + expiry));

        return ResponseEntity.ok(Map.of(
            "success", true,
            "message", "REST polling feed started: " + instrument + " " + expiry
        ));
    }

    // ── SESSION STATUS ────────────────────────────────────────────────────────
    @GetMapping("/session")
    public ResponseEntity<Map<String, Object>> session() {
        try {
            log.info("Session endpoint called - checking session status");
            
            var s = angelService.getSession();
            if (s == null) {
                log.warn("Session object is null");
                return ResponseEntity.ok(Map.of(
                    "loggedIn", false,
                    "name", "",
                    "pollingRunning", false,
                    "instrument", "",
                    "expiry", ""
                ));
            }
            
            Map<String, Object> response = Map.of(
                "loggedIn", s.isLoggedIn(),
                "name", s.getName() != null ? s.getName() : "",
                "pollingRunning", restPollingService.isRunning(),
                "instrument", restPollingService.getCurrentInstrument() != null ? restPollingService.getCurrentInstrument() : "",
                "expiry", restPollingService.getCurrentExpiry() != null ? restPollingService.getCurrentExpiry() : ""
            );
            
            log.info("Session response: {}", response);
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            log.error("Session endpoint error: ", e);
            return ResponseEntity.status(500).body(Map.of(
                "error", "Internal server error",
                "message", e.getMessage(),
                "timestamp", System.currentTimeMillis()
            ));
        }
    }

    // ── AI ANALYSIS ───────────────────────────────────────────────────────────
    @PostMapping("/analyze")
    public ResponseEntity<com.ocsentinel.model.AnalysisResult> analyze() {
        OCUpdate oc = restPollingService.getLatestOC();
        if (oc == null) {
            com.ocsentinel.model.AnalysisResult err = new com.ocsentinel.model.AnalysisResult();
            err.setVerdict("WAITING");
            err.setReasoning("Waiting for REST polling data feed...");
            return ResponseEntity.ok(err);
        }
        return ResponseEntity.ok(aiService.analyze(oc));
    }
}
