package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Access;
import com.example.locavel.domain.enums.Grade;
import com.example.locavel.domain.enums.Role;
import com.example.locavel.domain.enums.SocialType;
import com.example.locavel.domain.mapping.TermAgreement;
import com.example.locavel.domain.mapping.UserRegion;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private Grade localGrade = Grade.IRON;

    @Enumerated(EnumType.STRING)
    private Grade travelerGrade = Grade.IRON;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "region_id")
    private Region my_area;


    @PrePersist
    public void prePersist() {
        if (localGrade == null) {
            localGrade = Grade.IRON; // 기본값 설정
        }
        if (travelerGrade == null) {
            travelerGrade = Grade.IRON; // 기본값 설정
        }
    }


    private int localGradeScore; // 로컬 등급 점수
    private int travelerGradeScore; // 여행객 등급 점수

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private Access access;

    private LocalDateTime created_at;

    private LocalDateTime certified_at;//인증한 날짜

    private LocalDateTime deleted_at;

    private LocalDateTime updated_at;
    @Builder.Default
    private Integer followingCount = 0;
    @Builder.Default
    private Integer followerCount = 0;
    @Builder.Default
    private Integer reviewCount = 0;

    private int lastCalculatedMonths;

    @Builder.Default
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<TermAgreement> termAgreementList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Follow> followList = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserRegion> userRegionList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();

    //유저 권한 GUEST -> USER 로
    public void authorizeUser(){
        this.access = Access.USER;
    }

    //비밀번호 암호화
    public void passwordEncode(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
    }

    //필요한 setter 만 생성할 것
    public void updateRefreshToken(String updateRefreshToken){
        this.refreshToken = updateRefreshToken;
    }

    public void setProfileImage(String profileImage){
        this.profileImage = profileImage;
    }

    public void setUserName(String username){this.username = username;}

    public void setNickname(String nickname){this.nickname = nickname;}

    public void setIntroduce(String introduce){this.introduce = introduce;}

    public void setPhone_num(String phone_num){this.phone_num = phone_num;}


    // 관심 지역 추가 메서드
    public void addUserRegionList(UserRegion userRegion) {
        userRegionList.add(userRegion);
    }


    public void setLocalGrade(Grade localGrade) {
        this.localGrade = localGrade;
    }


    public void setLocalGradeScore(int localGradeScore) {
        this.localGradeScore = localGradeScore;
    }

    public void setTravelerGradeScore(int travelerGradeScore) {
        this.travelerGradeScore = travelerGradeScore;
    }


    public void setLastCalculatedMonths(int lastCalculatedMonths) {
        this.lastCalculatedMonths = lastCalculatedMonths;
    }

    public void setTravelerGrade(Grade travelerGrade) {
        this.travelerGrade = travelerGrade;
    }

    public void setFollowingCountPlus(){this.followingCount++;}
    public void setFollowerCountPlus(){this.followerCount++;}
    public void setFollowingCountMinus(){this.followingCount--;}
    public void setFollowerCountMinus(){this.followerCount--;}
    public void setReviewCountPlus(){this.reviewCount++;}
    public void setReviewCountMinus(){this.reviewCount--;}

    public void setMy_area(Region region) {
        this.my_area = region;
    }

}
