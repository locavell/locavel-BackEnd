package com.example.locavel.web.dto.ReviewDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
public class ReviewRequestDTO {
    @Getter
    @Setter
    public static class ReviewDTO {
        String comment;
        @NotNull
        Float rating;
    }
}
