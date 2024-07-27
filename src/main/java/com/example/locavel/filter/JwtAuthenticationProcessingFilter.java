package com.example.locavel.filter;

import com.example.locavel.domain.User;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.service.jwtService.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//RTR 방식으로 제작
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    //해당 URL 이외의 요청을 보내면 해당 토큰들의 유효성 검사하여 인증 처리할 것
    private static final String NO_CHECK_URL ="/api/login";

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response); //"/api/login" 요청이 들어오면, 다음 필터 호출
            return;//return은 필터 진행 막는 역할을 함
        }
        /**
        * 요청 헤더에서 RefreshToken 추출 이후 헤더에 RefreshToken이 없거나 DB에 저장된 값과 다르면 null을 반환
         * 요청 헤더에 RefreshToken 이 있다 -> AccessToken이 만료되어 요청한 경우
         * 위 경우를 제외하면 추출한 refreshToken은 모두 null이다.
        * */
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        //RefreshToken이 요청 헤더에 있으면 DB의 토큰 값과 일치하는지 확인후 일치하면 AccessToken 재발급
        if (refreshToken != null){
            checkRefreshTokenAndReIssueAccessToken (response, refreshToken);
            return;
        }
        //RefreshToken이 없거나 유효하지 않으면 AccessToken 검사하고 인증
        //만약 AccessToken 없거나 유효하지 않으면 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가 403 발생
        //AccessToken 유효하면, 인증 객체가 담긴 상태로 다음 필터로 넘어가므로 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }

    /**
     *  리프레시 토큰으로 유저 정보 찾기 & 액세스 토큰/리프레시 토큰 재발급 메소드
     *  파라미터로 들어온 헤더에서 추출한 리프레시 토큰으로 DB에서 유저를 찾고, 해당 유저가 있다면
     *  JwtService.createAccessToken()으로 AccessToken 생성,
     *  reIssueRefreshToken()로 리프레시 토큰 재발급 & DB에 리프레시 토큰 업데이트 메소드 호출
     *  그 후 JwtService.sendAccessTokenAndRefreshToken()으로 응답 헤더에 보내기
     */
    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userRepository.findByRefreshToken(refreshToken)
                .ifPresent(user -> {
                    String reIssuedRefreshToken = reIssueRefreshToken(user);
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(user.getEmail()),
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(User user){
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        user.updateRefreshToken(reIssuedRefreshToken);
        userRepository.saveAndFlush(user);//재발급한 refreshToken 업데이트 후 Flush
        return reIssuedRefreshToken;
    }


}
