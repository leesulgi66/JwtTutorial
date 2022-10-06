package com.example.jwttutorial.controller;

import com.example.jwttutorial.dto.LoginDto;
import com.example.jwttutorial.dto.TokenDto;
import com.example.jwttutorial.dto.TokenResponseDto;
import com.example.jwttutorial.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login") //login api 경로
    public ResponseEntity<TokenResponseDto> login(@Valid @RequestBody LoginDto loginDto) {

        return userService.getTokenDtoResponseEntity(loginDto);
    }
}