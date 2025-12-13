package ecom.ble.health.passport.controller.impl;

import com.fasterxml.jackson.databind.JsonNode;
import ecom.ble.health.passport.analyzer.AnalyzerRequest;
import ecom.ble.health.passport.analyzer.HealthReportAnalyzerService;
import ecom.ble.health.passport.analyzer.PrescriptionAnalyzerService;
import ecom.ble.health.passport.controller.AnalyzerController;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class DefaultAnalyzerController implements AnalyzerController {

    private final HealthReportAnalyzerService healthReportAnalyzerService;
    private final PrescriptionAnalyzerService prescriptionAnalyzerService;

    @Override
    public ResponseEntity<JsonNode> analyzeHealthReport(String userId, String filename, AnalyzerRequest request) {
        return ResponseEntity.ok(healthReportAnalyzerService.analyze(userId, filename, request));
    }

    @Override
    public ResponseEntity<String> analyzePrescription(String userId, String filename, AnalyzerRequest request) {
        return ResponseEntity.ok(prescriptionAnalyzerService.analyze(userId, filename, request));
    }
}


