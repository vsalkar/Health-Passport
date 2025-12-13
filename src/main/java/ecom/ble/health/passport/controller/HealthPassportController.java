package ecom.ble.health.passport.controller;

import ecom.ble.health.passport.model.User;
import ecom.ble.health.passport.model.record.MedicalRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface HealthPassportController {
    @RequestMapping("/user/{userId}")
    @ResponseBody
    @GetMapping
    abstract ResponseEntity<User> getUserHealthDetails(@PathVariable String userId);

    @RequestMapping("/record/add/{userId}")
    @ResponseBody
    @PostMapping
    abstract ResponseEntity<HttpStatus> addMedicalRecord(@RequestBody MedicalRecord record, @PathVariable String userId);

    @RequestMapping("/user/record/{id}")
    @ResponseBody
    @GetMapping
    abstract ResponseEntity<MedicalRecord> getRecordUserHealthDetails(@PathVariable String id);
}
