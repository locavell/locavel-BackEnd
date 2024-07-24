package com.example.locavel.service;

import com.example.locavel.web.dto.UserDTO.UserSignUpDto;

public interface UserCommandService {
    void signUp(UserSignUpDto userSignUpDto) throws Exception;
}
