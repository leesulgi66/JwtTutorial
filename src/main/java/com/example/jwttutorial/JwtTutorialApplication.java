package com.example.jwttutorial;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class JwtTutorialApplication {

    public static void main(String[] args) {
        SpringApplication.run(JwtTutorialApplication.class, args);
    }

}
