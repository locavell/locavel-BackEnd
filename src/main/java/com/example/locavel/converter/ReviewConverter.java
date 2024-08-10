package com.example.locavel.converter;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.ReviewImg;
import com.example.locavel.domain.Reviews;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Traveler;
import com.example.locavel.repository.ReviewImgRepository;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
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
                .user(user)
                .traveler(traveler)
                .place(place)
                .rating(request.getRating())
                .build();
    }
    public static ReviewImg toReviewImg(Reviews reviews, String url) {
        return ReviewImg.builder()
                .reviews(reviews)
                .imgUrl(url)
                .build();
    }
    public static ReviewResponseDTO.ReviewUpdateResultDTO toReviewUpdateResultDTO(Reviews review) {
        return ReviewResponseDTO.ReviewUpdateResultDTO.builder()
                .reviewId(review.getId())
                .updatedAt(LocalDateTime.now())
                .build();
    }
    public static ReviewResponseDTO.ReviewPreviewDTO toReviewPreviewDTO(
            Reviews review, List<String> reviewImgList) {
        User user = review.getUser();
        return ReviewResponseDTO.ReviewPreviewDTO.builder()
                .reviewId(review.getId())
                .reviewerImg(user.getProfileImage())
                .reviewerName(user.getNickname())
                .traveler(Traveler.of(review.getPlace(),user).getViewName())
                .reviewImgList(reviewImgList)
                .Rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreated_at())
                .updatedAt(review.getUpdated_at())
                .build();
    }
    public ReviewResponseDTO.ReviewPreviewListDTO toReviewPreviewListDTO(Page<Reviews> reviewList) {
        List<ReviewResponseDTO.ReviewPreviewDTO> reviewPreViewDTOList = reviewList.stream()
                .map(review -> {
                    List<String> reviewImgList = getAllReviewImgUrls(review);
                    return toReviewPreviewDTO(review,reviewImgList);
                }).collect(Collectors.toList());
        return ReviewResponseDTO.ReviewPreviewListDTO.builder()
                .reviewList(reviewPreViewDTOList)
                .listSize(reviewList.getSize())
                .isFirst(reviewList.isFirst())
                .isLast(reviewList.isLast())
                .totalElements(reviewList.getTotalElements())
                .totalPage(reviewList.getTotalPages())
                .build();
    }

    public static ReviewResponseDTO.MyReviewDTO toMyReviewDTO(
            Reviews review, List<String> reviewImgList) {
        Places place = review.getPlace();
        return ReviewResponseDTO.MyReviewDTO.builder()
                .reviewId(review.getId())
                .reviewImgList(reviewImgList)
                .address(place.getAddress())
                .placeName(place.getName())
                .createdAt(review.getCreated_at())
                .updatedAt(review.getUpdated_at())
                .comment(review.getComment())
                .Rating(review.getRating())
                .build();
    }

    public ReviewResponseDTO.MyReviewListDTO toMyReviewListDTO(Page<Reviews> reviewList) {
        List<ReviewResponseDTO.MyReviewDTO> myReviewDTOList = reviewList.stream()
                .map(review ->{
                    List<String> reviewImgList = getAllReviewImgUrls(review);
                    return toMyReviewDTO(review,reviewImgList);
                }).collect(Collectors.toList());
        return ReviewResponseDTO.MyReviewListDTO.builder()
                .isFirst(reviewList.isFirst())
                .isLast(reviewList.isLast())
                .listSize(reviewList.getSize())
                .reviewList(myReviewDTOList)
                .totalElements(reviewList.getTotalElements())
                .totalPage(reviewList.getTotalPages())
                .build();
    }

    public static ReviewResponseDTO.placeReviewSummaryDTO toPlaceReviewSummaryDTO(
            Float totalRating,
            Long totalReviewCount,
            Float generalRating,
            Long generalReviewCount,
            Float travelerRating,
            Long travelerReviewCount) {
        return ReviewResponseDTO.placeReviewSummaryDTO.builder()
                .totalRating(totalRating)
                .totalReviewCount(totalReviewCount)
                .generalRating(generalRating)
                .generalReviewCount(generalReviewCount)
                .travelerRating(travelerRating)
                .travelerReviewCount(travelerReviewCount)
                .build();
    }

    public static ReviewResponseDTO.ReviewDetailDTO toReviewDetailDTO(
            Reviews review,
            List<String> reviewImgList) {
        User user = review.getUser();
        Places place = review.getPlace();
        return ReviewResponseDTO.ReviewDetailDTO.builder()
                .reviewerImg(user.getProfileImage())
                .reviewerName(user.getNickname())
                .reviewId(review.getId())
                .address(place.getAddress())
                .Rating(review.getRating())
                .traveler(review.getTraveler().getViewName())
                .reviewImgList(reviewImgList)
                .updatedAt(review.getUpdated_at())
                .createdAt(review.getCreated_at())
                .placeName(place.getName())
                .comment(review.getComment())
                .build();
    }

    private final ReviewImgRepository reviewImgRepository;
    public List<String> getAllReviewImgUrls(Reviews review) {
        List<ReviewImg> reviewImgList = reviewImgRepository.findAllByReviews(review);
        List<String> reviewImgUrlList = new ArrayList<>();
        for(ReviewImg reviewImg : reviewImgList) {
            reviewImgUrlList.add(reviewImg.getImgUrl());
        }
        return reviewImgUrlList;
    }
}
