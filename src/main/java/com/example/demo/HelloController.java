package com.example.demo;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @Value("${app.version:1.0.0}")
    private String version;

    @GetMapping("/")
    public Map<String, String> hello() {
        return Map.of("message", "Hello from the demo app", "version", version);
    }
}
