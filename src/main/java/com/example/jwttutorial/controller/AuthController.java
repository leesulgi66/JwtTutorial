package com.example.jwttutorial.controller;

import com.example.jwttutorial.dto.LoginDto;
import com.example.jwttutorial.dto.TokenResponseDto;
import com.example.jwttutorial.service.SecurityService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final SecurityService securityService;

    public AuthController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @PostMapping("/login") //login api 경로
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        return securityService.getTokenDtoResponseEntity(loginDto);
    }
}