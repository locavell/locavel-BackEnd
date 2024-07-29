package com.example.locavel.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class CustomJsonUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final String DEFAULT_LOGIN_REQUEST_URL = "/api/login";//"/api/login"으로 오는 요청 처리
    private static final String HTTP_METHOD = "POST";// 로그인 HTTP 메소드 타입
    private static final String CONTENT_TYPE = "application/json";// JSON 타입의 데이터만 로그인 요청 처리
    private static final String USERNAME_KEY = "email";//회원 로그인 시 이메일 요청
    private static final String PASSWORD_KEY = "password";//회원 로그인 시 비밀번호 요청
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER = new AntPathRequestMatcher(DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD);
    private final ObjectMapper objectMapper;

    public CustomJsonUsernamePasswordAuthenticationFilter(ObjectMapper objectMapper){
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
        this.objectMapper = objectMapper;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException{
        if(request.getContentType() == null || request.getContentType().equals(CONTENT_TYPE)){
            throw new AuthenticationServiceException("Authentication Content-Type 이 제공되지 않습니다: "+ request.getContentType());
        }
        String messageBody = StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        //Map의 Key(email, password)로 해당 이메일, 패스워드 추출 후
        //UsernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
        Map<String, String> usernamePasswordMap = objectMapper.readValue(messageBody, Map.class);

        String email = usernamePasswordMap.get(USERNAME_KEY);
        String password = usernamePasswordMap.get(PASSWORD_KEY);

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(email, password);//principal, credentials 전달

        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
