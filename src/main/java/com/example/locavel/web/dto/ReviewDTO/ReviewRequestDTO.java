package com.example.locavel.web.dto.ReviewDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ReviewRequestDTO {
    @Getter
    @Setter
    public static class ReviewDTO {
        String comment;
        @NotNull
        Float rating;
        List<MultipartFile> img;
    }
}
