package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.SuccessStatus;
import com.example.locavel.converter.UserConverter;
import com.example.locavel.domain.User;
import com.example.locavel.service.userService.UserCommandService;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;
import com.example.locavel.web.dto.UserDTO.UserSignUpDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserCommandService userCommandService;
    private final UserConverter userConverter;

    @PostMapping("/api/auth/sign-up")
    public String signUp(@RequestBody UserSignUpDto userSignUpDto) throws Exception{
        userCommandService.signUp(userSignUpDto);
        return "sign-up Success";
    }

    @GetMapping("/jwt-test")//테스트용
    public String jwtTest(){
        return "jwt-Test Success";
    }

    @PostMapping("/api/auth/profile")
    public ApiResponse<UserResponseDto.UpdateUserProfileResultDTO> updateUserProfile(HttpServletRequest httpServletRequest, @RequestBody UserRequestDto.UpdateUserProfileDto request){
        User user = userCommandService.updateUserProfile(httpServletRequest, request);
        return ApiResponse.of(SuccessStatus.USER_PROFILE_UPDATED, UserConverter.updateUserProfileResultDTO(user));
    }

    @GetMapping("/api/auth")
    public ApiResponse<UserResponseDto.getUserDTO> getUser(HttpServletRequest httpServletRequest){
        User user = userCommandService.getUser(httpServletRequest);
        return ApiResponse.of(SuccessStatus.USER_FOUND, userConverter.toGetUserResultDTO(user));
    }
}
