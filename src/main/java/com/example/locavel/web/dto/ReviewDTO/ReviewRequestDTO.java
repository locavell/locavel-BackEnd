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
        String comment;
        @NotNull
        Float rating;
    }
}
