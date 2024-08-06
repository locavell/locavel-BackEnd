package com.example.locavel.converter;

import com.example.locavel.domain.User;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;

public class UserConverter {

    public static UserResponseDto.UpdateUserProfileResultDTO updateUserProfileResultDTO(User user){
        return UserResponseDto.UpdateUserProfileResultDTO.builder()
                .profileImage(user.getProfileImage())
                .build();
    }
}
