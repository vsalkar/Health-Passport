package ecom.ble.health.passport.controller;

import com.fasterxml.jackson.databind.JsonNode;
import ecom.ble.health.passport.analyzer.AnalyzerRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/api/users/{userId}/analyze")
public interface AnalyzerController {

    @PostMapping("/health-report/{filename:.+}")
    ResponseEntity<JsonNode> analyzeHealthReport(
            @PathVariable String userId,
            @PathVariable String filename,
            @RequestBody(required = false) AnalyzerRequest request);

    @PostMapping("/prescription/{filename:.+}")
    ResponseEntity<String> analyzePrescription(
            @PathVariable String userId,
            @PathVariable String filename,
            @RequestBody(required = false) AnalyzerRequest request);
}


