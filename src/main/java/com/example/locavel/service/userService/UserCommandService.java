package com.example.locavel.service.userService;

import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Grade;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;
import com.example.locavel.web.dto.UserDTO.UserSignUpDto;
import jakarta.servlet.http.HttpServletRequest;

public interface UserCommandService {
    public User getUser(HttpServletRequest httpServletRequest);
    public User findUser(Long id);
    void signUp(UserSignUpDto userSignUpDto) throws Exception;

    User updateUserProfile(HttpServletRequest httpServletRequest, UserRequestDto.UpdateUserProfileDto request);

    User deleteUser(HttpServletRequest httpServletRequest);

    User updateUser(HttpServletRequest httpServletRequest, UserRequestDto.UpdateUserDTO updateUserDTO);

    User getUserGrade(Long userId);

    User updateLocalGrade(Long userId);

    void updateMemberScoresDaily();

    User calculateTravelerGradeScore(Long userId, ReviewRequestDTO.ReviewDTO request);

    Grade calculateLocalGrade(int score);
}
