package com.example.locavel.converter;

import com.example.locavel.domain.User;
import com.example.locavel.web.dto.UserDTO.UserRequestDto;
import com.example.locavel.web.dto.UserDTO.UserResponseDto;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

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
                .localGrade(user.getLocalGrade())
                .travelerGrade(user.getTravelerGrade())
                .role(user.getRole())
                .created_at(user.getCreated_at())
                .updated_at(user.getUpdated_at())
                .build();
    }

    public UserResponseDto.DeleteUserResultDTO toDeleteResultDTO(User user){
        return UserResponseDto.DeleteUserResultDTO.builder()
                .user_id(user.getId())
                .deleted_at(LocalDateTime.now())
                .build();
    }

    public UserResponseDto.UpdateUserDTO toUpdateUserDTO(User user){
        return UserResponseDto.UpdateUserDTO.builder()
                .user_id(user.getId())
                .name(user.getUsername())
                .nickname(user.getNickname())
                .introduce(user.getIntroduce())
                .phone_num(user.getPhone_num())
                .updated_at(LocalDateTime.now())
                .build();
    }

    //회원 등급 조회하기
    public  UserResponseDto.GradeResponseDto toGetUserGradeDTO(User user){
        return UserResponseDto.GradeResponseDto.builder()
                .userId(user.getId())
                .localGrade(user.getLocalGrade())
                .travelerGrade(user.getTravelerGrade())
                .build();
    }






}
