package com.sparta.springassignment01.repository;

import com.sparta.springassignment01.entity.Member;
import com.sparta.springassignment01.jwt.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMember(Member member);
}
