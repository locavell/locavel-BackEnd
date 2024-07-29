package com.example.locavel.web.dto.ReviewDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

public class ReviewRequestDTO {
    @Getter
    @Setter
    public static class RevieweDTO {
        //TODO :  JWT 토큰 추가 후 userId 삭제
        Long userId;
        //TODO : S3 추가 후 수정
//        List<MultipartFile> reviewImgList;
        String comment;
        @NotNull
        Float rating;
    }
}
