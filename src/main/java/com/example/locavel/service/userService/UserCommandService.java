package com.example.locavel.service.userService;

import com.example.locavel.domain.User;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserSignUpDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserCommandService {
    void signUp(UserSignUpDto userSignUpDto) throws Exception;

    User updateUserProfile(HttpServletRequest httpServletRequest, UserRequestDto.UpdateUserProfileDto request);
}
