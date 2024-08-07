package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Access;
import com.example.locavel.domain.enums.Grade;
import com.example.locavel.domain.enums.Role;
import com.example.locavel.domain.enums.SocialType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String refreshToken;//추가한 칼럼

    private String email;//추가한 칼럼

    private String socialId;//추가한 칼럼

    private String password;//추가한 칼럼

    private String profileImage;//추가한 칼럼

    private String location;//인증된 위치

    private String username;

    private String nickname;

    private String introduce;

    private String phone_num;

    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Access access;

    private LocalDateTime created_at;

    private LocalDateTime certified_at;//인증한 날짜

    private LocalDateTime deleted_at;

    //유저 권한 GUEST -> USER 로
    public void authorizeUser(){
        this.access = Access.USER;
    }

    //비밀번호 암호화
    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateRefreshToken(String updateRefreshToken){
        this.refreshToken = updateRefreshToken;
    }

    public void setProfileImage(String profileImage){
        this.profileImage = profileImage;
    }
}
