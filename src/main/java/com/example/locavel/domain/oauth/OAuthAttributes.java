package com.example.locavel.domain.oauth;

import com.example.locavel.info.OAuth2UserInfo;
import lombok.Builder;
import lombok.Getter;

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
}
