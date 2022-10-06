package com.example.jwttutorial.repository;

import com.example.jwttutorial.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenJpaRepository extends JpaRepository<RefreshToken, String> {
    Optional<RefreshToken> findBykey(Long key);
}
