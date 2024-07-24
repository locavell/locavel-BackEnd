package com.example.locavel.web.dto.UserDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UserSignUpDto {

    private String email;
    private String password;
    private String username;
    private String nickname;
    private String introduce;
    private String phone_num;
}
