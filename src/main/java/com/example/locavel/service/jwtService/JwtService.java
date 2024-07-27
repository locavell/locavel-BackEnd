package com.example.locavel.service.jwtService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.locavel.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class JwtService {

    private final UserRepository userRepository;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    /**
     * AccessToken 생성 메소드
     * JWT의 Subject -> AccessToken
     * JWT의 Claim -> email, 만약 식별자 필요하다면 withClaim(클래임 이름, 클래임 값) 넣어서 추가 가능
     */
    public String createAccessToken(String email){
        Date now = new Date();
        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + accessTokenExpirationPeriod))//액세스 토큰 만료 시간 설정
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * RefreshToken 생성 메소드
     * JWT의 Subject -> RefreshToken
     * JWT의 Claim -> email 넣지 않으므로 필요 없음
     */

    public String createRefreshToken(){
        Date now = new Date();
        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(new Date(now.getTime() + refreshTokenExpirationPeriod))//리프레시 토큰 만료 시간 설정
                .sign(Algorithm.HMAC512(secretKey));
    }

    /**
     * AccessToken 헤더에 담아서 보내기
     * */
    public void sendAccessToken(HttpServletResponse response, String accessToken){
        response.setStatus(HttpServletResponse.SC_OK);//200 상태 코드
        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 AccessToken : {}", accessToken);//액세스 토큰 확인
    }
    /**
     * AccessToken + RefreshToken 헤더에 담아서 보내기
     * */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken){
        response.setStatus(HttpServletResponse.SC_OK);//200 상태 코드
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("AccessToken, RefreshToken 헤더 설정 성공");
    }
}
