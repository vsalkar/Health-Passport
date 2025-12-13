package ecom.ble.health.passport.controller;

import ecom.ble.health.passport.model.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

public interface HealthPassportController {
    @RequestMapping("/user/{id}")
    @ResponseBody
    @GetMapping
    abstract ResponseEntity<User> getUserHealthDetails(@PathVariable String id);
}
