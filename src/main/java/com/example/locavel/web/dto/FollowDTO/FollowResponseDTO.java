package com.example.locavel.web.dto.FollowDTO;

import com.example.locavel.domain.enums.Grade;
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
        Grade localGrade;
        Grade travelerGrade;

    }
}
