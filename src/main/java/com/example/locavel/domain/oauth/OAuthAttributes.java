package com.example.locavel.domain.oauth;

import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Access;
import com.example.locavel.domain.enums.SocialType;
import com.example.locavel.info.GoogleOAuth2UserInfo;
import com.example.locavel.info.KakaoOAuth2UserInfo;
import com.example.locavel.info.NaverOAuth2UserInfo;
import com.example.locavel.info.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

//DTO 클래스라고 봐도 무방함
@Getter
public class OAuthAttributes {

    private String nameAttributeKey;//OAuth2 로그인 할 때, 키가 되는 필드
    private OAuth2UserInfo oAuth2UserInfo;//소셜 타입별 로그인 유저 정보

    @Builder
    private OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo){
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static OAuthAttributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes){
        if (socialType == SocialType.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        if (socialType == SocialType.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        return ofGoogle(userNameAttributeName, attributes);
    }

    private static OAuthAttributes ofKakao(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    public static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    //수정 예정ㅒ
    public User toEntity(SocialType socialType, OAuth2UserInfo oAuth2UserInfo){
        return User.builder()
                .socialType(socialType)
                .socialId(oAuth2UserInfo.getId())
                .email(UUID.randomUUID() + "socialUser.com")
                .nickname(oAuth2UserInfo.getNickname())
                .access(Access.GUEST)
                .build();
    }
}
