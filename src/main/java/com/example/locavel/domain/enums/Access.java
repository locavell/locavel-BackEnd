package com.example.locavel.domain.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Access {
    GUEST("ROLE_GUEST"),//첫 로그인 시 GUEST
    USER("ROLE_USER");//추가 정보 기입시 USER

    private final String key;
}
