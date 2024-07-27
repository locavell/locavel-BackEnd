package com.example.locavel.service.jwtService;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.locavel.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

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
    //헤더 설정 메서드
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken){
        response.setHeader(accessHeader, accessToken);
    }
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken){
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * AccessToken을 헤더에서 추출하고 Bearer 삭제하고 토큰값만 가져오기
     * */
    public Optional<String> extractAccessToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }
    //동일하게 RefreshToken 값만 가져오기
    public Optional<String> extractRefreshToken(HttpServletRequest request){
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출
     * 추출 전에 JWT.require()로 검증기 생성
     * verify로 AceessToken 검증 후
     * 유효하다면 getClaim()으로 이메일 추출
     * 유효하지 않다면 빈 Optional 객체 반환
     */
    public Optional<String> extractEmail(String accessToken){
        try{
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)//accessToken 유효한지 검증
                    .getClaim(EMAIL_CLAIM)//claim에 담긴 email 가져오기
                    .asString());

        }catch (Exception e){
            log.error("엑세스 토큰이 유효하지 않습니다.");
            return Optional.empty();
        }
    }

     /**
     * RefresshToken DB에 저장
     * */

     public void updateRefreshToken(String email, String refreshToken){
         userRepository.findByEmail(email)
                 .ifPresentOrElse(
                         user -> user.updateRefreshToken(refreshToken),
                         () -> new Exception("일치하는 회원이 없습니다.")
                 );
     }
}
