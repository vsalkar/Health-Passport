package ecom.ble.health.passport.controller;

import ecom.ble.health.passport.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

public interface HealthPassportController {
    @RequestMapping("/user/{userId}")
    @ResponseBody
    @GetMapping
    abstract ResponseEntity<User> getUserHealthDetails(@PathVariable String userId);

    @RequestMapping("/users/{userId}/record/{fileid}")
    @ResponseBody
    @GetMapping
    abstract ResponseEntity<String> getRecordUserHealthDetails(@PathVariable String fileid, @PathVariable String userId);
}
