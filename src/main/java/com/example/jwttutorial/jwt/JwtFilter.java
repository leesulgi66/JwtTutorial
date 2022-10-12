package com.example.jwttutorial.jwt;

import com.example.jwttutorial.dto.TokenDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtFilter extends OncePerRequestFilter {
    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);

    public static final String AUTHORIZATION_HEADER = "Authorization";

    private TokenProvider tokenProvider;

    //tokenProvider를 주입받는다.
    public JwtFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }


    //GenericFilterBean의 doFilter를 Override. 실제 필터링 로직은 doFilter 내부에 작성
    //doFilter는 토큰의 인증정보를 Securitycontext에 저장하는 역할 수행!!
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwt = resolveToken(request);
        String requestURI = request.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == TokenProvider.JwtCode.ACCESS) { //받아온 토큰이 유효성 검증이 완료되면
            Authentication authentication = tokenProvider.getAuthentication(jwt); //authentication 객체를 반환하고
            SecurityContextHolder.getContext().setAuthentication(authentication); //securityContextHolder에 저장해준다
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }
        else if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == TokenProvider.JwtCode.EXPIRED){
            logger.info("만료된 토큰 확인");
            System.out.println("-만료된 토큰 : " + jwt);
            String username = tokenProvider.JwtUsername(jwt);
            String refreshToken = tokenProvider.userToken(username);
            if(tokenProvider.validateToken(refreshToken) == TokenProvider.JwtCode.ACCESS) {
                logger.info("새로운 토큰으로 발급");
                Authentication authentication = tokenProvider.getAuthentication(refreshToken);
                TokenDto reJwt = tokenProvider.createToken(authentication);
                System.out.println("-새토큰 : " + reJwt.getAccessToken());

                response.addHeader(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + reJwt.getAccessToken());

            } else{
                logger.info("유효한 refreshToken이 없습니다. {}" , "다시 로그인 해주세요");
            }
        }
        else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(request, response);
    }

    //Request Header에서 토큰 정보를 꺼내오기 위한메소드
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
