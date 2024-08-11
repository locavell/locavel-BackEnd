package com.example.locavel.converter;

import com.example.locavel.domain.Follow;
import com.example.locavel.domain.User;
import com.example.locavel.web.dto.FollowDTO.FollowResponseDTO;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

public class FollowConverter {
    public static Follow toFollow(User user, Long followUserId) {
        return Follow.builder()
                .user(user)
                .followUserId(followUserId)
                .build();
    }
    public static FollowResponseDTO.FollowPreviewDTO toFollowPreviewDTO(User user) {
        return FollowResponseDTO.FollowPreviewDTO.builder()
                .nickname(user.getNickname())
                .profile(user.getProfileImage())
                .userId(user.getId())
                //TODO:등급추가
                .build();
    }

    public static FollowResponseDTO.FollowPreviewListDTO toFollowPreviewListDTO(Page<User> followList) {
        List<FollowResponseDTO.FollowPreviewDTO> followPreviewListDTOList = followList.stream()
                .map(FollowConverter::toFollowPreviewDTO).collect(Collectors.toList());
        return FollowResponseDTO.FollowPreviewListDTO.builder()
                .followList(followPreviewListDTOList)
                .listSize(followList.getSize())
                .isFirst(followList.isFirst())
                .isLast(followList.isLast())
                .totalElements(followList.getTotalElements())
                .totalPage(followList.getTotalPages())
                .build();
    }
}
