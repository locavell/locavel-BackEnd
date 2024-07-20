package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Grade;
import com.example.locavel.domain.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDateTime;

@Entity
@Getter
@Builder
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    private String refreshToken;//추가한 칼럼

    private String email;//추가한 칼럼

    private String password;//추가한 칼럼

    private String profileImage;//추가한 칼럼

    private String location;//인증된 위치

    private String username;

    private String nickname;

    private String introduce;

    private String phone_num;

    private Grade grade;

    private Role role;

    private LocalDateTime created_at;

    private LocalDateTime certified_at;//인증한 날짜
}
