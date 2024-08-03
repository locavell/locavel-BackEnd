package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.code.status.SuccessStatus;
import com.example.locavel.apiPayload.exception.handler.ReviewsHandler;
import com.example.locavel.service.ReviewService;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "리뷰를 등록합니다.")
    @PostMapping(value = "/{placeId}", consumes = "multipart/form-data")
    public ApiResponse<ReviewResponseDTO.ReviewResultDTO> createReview(
            @Valid @RequestPart ReviewRequestDTO.RevieweDTO request,
            @PathVariable(name="placeId") Long placeId,
            @RequestPart(required = false) List<MultipartFile> reviewImgUrls) {
        if(request.getRating() == null) {
            throw new ReviewsHandler(ErrorStatus.RATING_NOT_EXIST);
        }
        if(request.getRating() > 5 || request.getRating() <0) {
            throw new ReviewsHandler(ErrorStatus.RATING_NOT_VALID);
        }
        ReviewResponseDTO.ReviewResultDTO response = reviewService.createReview(placeId, request, reviewImgUrls);
        return ApiResponse.of(SuccessStatus.REVIEW_CREATE_OK,response);
    }

    @Operation(summary = "리뷰 수정", description = "리뷰를 수정합니다.")
    @PatchMapping(value = "/{reviewId}", consumes = "multipart/form-data")
    public ApiResponse<ReviewResponseDTO.ReviewUpdateResultDTO> updateReview(
            @Valid @RequestPart ReviewRequestDTO.RevieweDTO request,
            @PathVariable(name="reviewId") Long reviewId,
            @RequestPart(required = false) List<MultipartFile> reviewImgUrls) {
        if(request.getRating() > 5 || request.getRating() <0) {
            throw new ReviewsHandler(ErrorStatus.RATING_NOT_VALID);
        }
        ReviewResponseDTO.ReviewUpdateResultDTO response = reviewService.updateReview(reviewId, request, reviewImgUrls);
        return ApiResponse.of(SuccessStatus.REVIEW_UPDATE_OK,response);
    }

    @Operation(summary = "리뷰 삭제", description = "리뷰를 삭제합니다.")
    @DeleteMapping(value = "/{reviewId}")
    public ApiResponse<ReviewResponseDTO.ReviewResultDTO> deleteReview(@PathVariable(name="reviewId")Long reviewId) {
        ReviewResponseDTO.ReviewResultDTO response = reviewService.deleteReview(reviewId);
        return ApiResponse.of(SuccessStatus.REVIEW_DELETE_OK,response);
    }
}
