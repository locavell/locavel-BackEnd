package com.example.locavel.repository;

import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.SocialType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    //OAuth2 로그인 구현 시 사용
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findByRefreshToken(String refreshToken);
}
