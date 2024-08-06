package com.example.locavel.service.userService;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Access;
import com.example.locavel.apiPayload.exception.handler.UserHandler;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.service.jwtService.JwtService;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserSignUpDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Transactional
@RequiredArgsConstructor
public class UserCommandServiceImpl implements UserCommandService{

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;//PasswordEncoder 구현체 Spring Bean 등록으로 해결
    private final JwtService jwtService;

    public void signUp(UserSignUpDto userSignUpDto) throws Exception{

        if (userRepository.findByEmail(userSignUpDto.getEmail()).isPresent()){
            throw new Exception("이미 존재하는 이메일 입니다.");
        }

        if (userRepository.findByNickname(userSignUpDto.getNickname()).isPresent()){
            throw new Exception("이미 존재하는 닉내임 입니다.");
        }
        //추가적으로 전화번호로 찾는 로직 필요하면 추가할 예정

        User user = User.builder()
                .email(userSignUpDto.getEmail())
                .password(userSignUpDto.getPassword())
                .nickname(userSignUpDto.getNickname())
                .username(userSignUpDto.getUsername())
                .introduce(userSignUpDto.getIntroduce())
                .phone_num(userSignUpDto.getPhone_num())
                .access(Access.USER)
                .created_at(LocalDateTime.now())
                .build();

        user.passwordEncode(passwordEncoder);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUserProfile(HttpServletRequest httpServletRequest, UserRequestDto.UpdateUserProfileDto updateUserProfileDto){
        String email = httpServletRequest.getUserPrincipal().getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserHandler(ErrorStatus.USER_NOT_FOUND));
        if(updateUserProfileDto.getProfileImage() != null) user.setProfileImage(updateUserProfileDto.getProfileImage());

        return user;
    }
}
