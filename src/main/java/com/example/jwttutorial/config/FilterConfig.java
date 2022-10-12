//package com.example.jwttutorial.config;
//
//import com.example.jwttutorial.jwt.JwtFilter;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@RequiredArgsConstructor
//public class FilterConfig {
//
//    private final JwtFilter jwtFilter;
//    @Bean
//    public FilterRegistrationBean filterRegistrationBean() {
//        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
//        registrationBean.setFilter(jwtFilter);
//        registrationBean.setEnabled(false);
//        return registrationBean;
//    }
//}
