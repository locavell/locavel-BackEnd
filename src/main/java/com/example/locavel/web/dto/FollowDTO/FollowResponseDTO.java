package com.example.locavel.web.dto.FollowDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class FollowResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowPreviewListDTO {
        List<FollowResponseDTO.FollowPreviewDTO> followList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FollowPreviewDTO {
        Long userId;
        String profile;
        String nickname;
        //TODO : 등급 기능 추가 후 등급 추가
//        Grade generalGrade;
//        Grade travelerGrade;

    }
}
