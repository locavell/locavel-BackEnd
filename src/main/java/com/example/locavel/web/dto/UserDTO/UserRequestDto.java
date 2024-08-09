package com.example.locavel.web.dto.UserDTO;

import lombok.Getter;

import java.time.LocalDateTime;

public class UserRequestDto {

    @Getter
    public static class UpdateUserProfileDto{
        private String profileImage;
    }

    @Getter
    public static class UpdateUserDTO{
        private String name;
        private String nickname;
        private String introduce;
        private String phone_num;
    }
}
