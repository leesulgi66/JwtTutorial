package com.example.jwttutorial.service;

import com.example.jwttutorial.dto.LoginDto;
import com.example.jwttutorial.dto.TokenDto;
import com.example.jwttutorial.dto.TokenResponseDto;
import com.example.jwttutorial.entity.RefreshToken;
import com.example.jwttutorial.entity.User;
import com.example.jwttutorial.jwt.JwtFilter;
import com.example.jwttutorial.jwt.TokenProvider;
import com.example.jwttutorial.repository.RefreshTokenJpaRepository;
import com.example.jwttutorial.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Key;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class SecurityService {

    private Key key;
    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final RefreshTokenJpaRepository refreshTokenJpaRepository;
    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<TokenResponseDto> getTokenDtoResponseEntity(LoginDto loginDto) {
        Optional<User> user = userRepository.findOneWithAuthoritiesByUsername(loginDto.getUsername());
        //dto로 받은 username과 password를 가지고 authenticationToken객체를 생성
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        //authenticate메소드가 실행이 될 때, CustomUserDetailsService의 loadUserByUsername이 실행이 된다. authentication객체 생성 후, ContextHolder에 저장된다.
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        //jwt 토큰 생성
        TokenDto jwt = tokenProvider.createToken(authentication);
        //refresh 정보 저장
        RefreshToken refreshToken = RefreshToken.builder()
                .key(user.get().getUserId())
                .token(jwt.getRefreshToken())
                .build();

        if(refreshTokenJpaRepository.findBykey(user.get().getUserId()).isEmpty()) {
            refreshTokenJpaRepository.save(refreshToken);
        }else {
            refreshTokenJpaRepository.deleteByKey(user.get().getUserId());
            refreshTokenJpaRepository.save(refreshToken);
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt.getAccessToken());

        return new ResponseEntity<>(TokenResponseDto.builder()
                .grantType("bearer ")
                .accessToken(jwt.getAccessToken())
                .accessTokenExpireDate(jwt.getAccessTokenExpireDate())
                .build(), httpHeaders, HttpStatus.OK);
    }

    public String reissue(String token) {
        return token;
    }
}
