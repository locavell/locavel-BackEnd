package com.example.locavel.web.dto.UserDTO;

import lombok.Getter;

public class UserRequestDto {

    @Getter
    public static class UpdateUserProfileDto{
        private String profileImage;
    }
}
