package com.example.locavel.converter;

import com.example.locavel.domain.User;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;
import org.springframework.stereotype.Component;

@Component
public class UserConverter {

    public static UserResponseDto.UpdateUserProfileResultDTO updateUserProfileResultDTO(User user){
        return UserResponseDto.UpdateUserProfileResultDTO.builder()
                .profileImage(user.getProfileImage())
                .build();
    }

    public UserResponseDto.getUserDTO toGetUserResultDTO(User user){
        return UserResponseDto.getUserDTO.builder()
                .user_id(user.getId())
                .email(user.getEmail())
                .introduce(user.getIntroduce())
                .profileImage(user.getProfileImage())
                .username(user.getUsername())
                .nickname(user.getNickname())
                .socialType(user.getSocialType())
                .grade(user.getGrade())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .build();
    }
}
