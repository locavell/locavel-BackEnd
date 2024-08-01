package com.example.locavel.domain.oauth;

import com.example.locavel.domain.enums.Access;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

@Getter
public class CustomOAuth2User extends DefaultOAuth2User {

    private String email;
    private Access access;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, String email, Access access){
        super(authorities, attributes, nameAttributeKey);
        this.email = email;
        this.access = access;
    }
}
