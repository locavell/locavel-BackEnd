package com.example.locavel.web.dto.ReviewDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewResultDTO {
        Long reviewId;
        LocalDateTime createdAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewUpdateResultDTO {
        Long reviewId;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewPreviewListDTO {
        List<ReviewPreviewDTO> reviewList;
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
    public static class ReviewPreviewDTO {
        Long reviewId;
        String traveler;
        String reviewerName;
        String reviewerImg;
        Integer reviewerFollowerCount;
        List<String> reviewImgList;
        String comment;
        Float Rating;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MyReviewListDTO {
        List<MyReviewDTO> reviewList;
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
    public static class MyReviewDTO {
        Long reviewId;
        String placeName;
        String address;
        List<String> reviewImgList;
        String comment;
        Float Rating;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class placeReviewSummaryDTO {
        Float totalRating;
        Long totalReviewCount;
        Float generalRating;
        Long generalReviewCount;
        Float travelerRating;
        Long travelerReviewCount;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDetailDTO {
        Long reviewId;
        String placeName;
        String address;
        List<String> reviewImgList;
        String comment;
        Float Rating;
        String traveler;
        String reviewerName;
        String reviewerImg;
        Integer reviewerFollowerCount;
        LocalDateTime createdAt;
        LocalDateTime updatedAt;
    }
}
