package com.example.locavel.converter;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.Reviews;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Traveler;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;

import java.time.LocalDateTime;

public class ReviewConverter {
    public static ReviewResponseDTO.ReviewResultDTO toReviewResultDTO(Reviews review) {
        return ReviewResponseDTO.ReviewResultDTO.builder()
                .reviewId(review.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
    public static Reviews toReviews(User user, Traveler traveler, Places place, ReviewRequestDTO.RevieweDTO request) {
        return Reviews.builder()
                .comment(request.getComment())
                //TODO : S3 추가 시 url로 변경
//                .reviewImg(null)
                .user(user)
                .traveler(traveler)
                .place(place)
                .rating(request.getRating())
                .build();
    }
}
