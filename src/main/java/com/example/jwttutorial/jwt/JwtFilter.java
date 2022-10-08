package com.example.jwttutorial.jwt;

import com.example.jwttutorial.entity.User;
import com.example.jwttutorial.repository.RefreshTokenJpaRepository;
import com.example.jwttutorial.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;


public class JwtFilter extends GenericFilterBean {
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == TokenProvider.JwtCode.ACCESS) { //받아온 토큰이 유효성 검증이 완료되면
            Authentication authentication = tokenProvider.getAuthentication(jwt); //authentication 객체를 반환하고
            SecurityContextHolder.getContext().setAuthentication(authentication); //securityContextHolder에 저장해준다
            logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
        }
        else if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt) == TokenProvider.JwtCode.EXPIRED){
            logger.info("만료된 토큰 확인");
            System.out.println(jwt);
            String username = tokenProvider.JwtUsername(jwt);
            System.out.println(username);
            Optional<User> user = tokenProvider.user(username);
            System.out.println(user);
//            RefreshToken refreshToken = refreshTokenJpaRepository.findBykey(user.getUserId()).orElseThrow(
//                    ()-> new NullPointerException("존재하는 refresh Token이 없습니다.")
//            );
//            String refreshTokenString = refreshToken.getToken();
//            System.out.println(refreshTokenString);
        }

        else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
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
