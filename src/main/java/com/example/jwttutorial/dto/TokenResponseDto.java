package com.example.jwttutorial.dto;

import lombok.*;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Component
public class TokenResponseDto {
    private String grantType;
    private String accessToken;
    private Long accessTokenExpireDate;
}
