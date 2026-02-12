package com.example.blog;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class HealthCheck {

    @GetMapping("/health-check")
    public String healthCheck() {
        return "Health is Good";
    }
}
