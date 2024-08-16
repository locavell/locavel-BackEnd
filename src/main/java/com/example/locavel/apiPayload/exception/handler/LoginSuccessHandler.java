package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.repository.UserRepository;
import com.example.locavel.service.jwtService.JwtService;
import com.example.locavel.web.dto.TokenDTO.TokenResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        try{
            String email = extractUsername(authentication);
            String accessToken = jwtService.createAccessToken(email);
            String refreshToken = jwtService.createRefreshToken();

            jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

            userRepository.findByEmail(email)
                    .ifPresent(user -> {
                        user.updateRefreshToken(refreshToken);
                        userRepository.saveAndFlush(user);
                    });
            log.info("로그인 성공. 이메일 : {}", email);
            log.info("로그인 성공. AccessToken: {}", accessToken);
            log.info("AccessToken 만료 기간: {}", accessTokenExpiration);

            TokenResponseDTO tokenResponse = new TokenResponseDTO(accessToken, refreshToken);

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            // JSON으로 변환하여 응답 전송
            objectMapper.writeValue(response.getOutputStream(), tokenResponse);

        } catch (IOException e){
            log.error("응답 작성 중 오류 발생: {}", e.getMessage());
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }


    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
