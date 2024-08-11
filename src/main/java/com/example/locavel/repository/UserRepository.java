package com.example.locavel.repository;

import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.SocialType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByNickname(String nickname);

    //OAuth2 로그인 구현 시 사용
    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    Optional<User> findByRefreshToken(String refreshToken);
    Page<User> findAllByIdIn(List<Long> userId, PageRequest pageRequest);
}
