package org.ngo.dashboard.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestController
public class HealthCheckController {

    @Value("${app.version:1.0.0}")
    private String appVersion;

    @Value("${app.environment:development}")
    private String environment;

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> health() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("service", "ngo-project-dashboard");
        response.put("version", appVersion);
        response.put("environment", environment);
        response.put("timestamp", Instant.now().toString());
        response.put("systemStatus", "OPERATIONAL");
        return ResponseEntity.ok(response);
    }
}
