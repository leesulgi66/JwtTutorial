package com.example.jwttutorial.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HelloController {
    @GetMapping("hello")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("hello");
    }
}
