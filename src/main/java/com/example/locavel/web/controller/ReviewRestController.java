package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.code.status.SuccessStatus;
import com.example.locavel.apiPayload.exception.handler.ReviewsHandler;
import com.example.locavel.converter.ReviewConverter;
import com.example.locavel.domain.Reviews;
import com.example.locavel.domain.enums.Traveler;
import com.example.locavel.service.ReviewService;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;
    private final ReviewConverter reviewConverter;

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

    @Operation(summary = "리뷰 목록 조회", description = "특정 장소의 리뷰 목록을 조회합니다.")
    @GetMapping(value="/places/{placeId}")
    @Parameters({
            @Parameter(name = "page", description = "페이지 번호, 1번이 1 페이지"),
            @Parameter(name = "sort", description = "최신순/오래된순 중 입력해주세요. 기본은 최신순입니다."),
            @Parameter(name = "filter", description = "여행객/현지인 중 입력해주세요. 입력하지 않으면 전체 조회입니다.")
    })
    public ApiResponse<ReviewResponseDTO.ReviewPreviewListDTO> getReviews(
            @PathVariable(name="placeId")Long placeId,
            @RequestParam(name = "page") Integer page,
            @RequestParam(required = false, name = "sort") String sortOption,
            @RequestParam(required = false, name = "filter") String travelerOption) {
        if(page<=0){
            throw new ReviewsHandler(ErrorStatus.PAGE_NOT_VALID);
        }
        Traveler traveler = Traveler.toTraveler(travelerOption);
        Page<Reviews> reviewList= reviewService.getReviewList(placeId,page-1,sortOption,traveler);
        ReviewResponseDTO.ReviewPreviewListDTO response = reviewConverter.toReviewPreviewListDTO(reviewList);
        return ApiResponse.of(SuccessStatus.REVIEW_GET_OK,response);
    }

    @Operation(summary = "유저 리뷰 목록 조회", description = "특정 유저가 작성한 리뷰 목록을 조회합니다.")
    @GetMapping(value = "/users/{userId}")
    public ApiResponse<ReviewResponseDTO.MyReviewListDTO> getMyReviews(
            @PathVariable(name="userId")Long userId,
            @RequestParam(name = "page") Integer page) {
        if(page<=0){
            throw new ReviewsHandler(ErrorStatus.PAGE_NOT_VALID);
        }
        Page<Reviews> myReviewList = reviewService.getMyReviewList(userId,page-1);
        ReviewResponseDTO.MyReviewListDTO response = reviewConverter.toMyReviewListDTO(myReviewList);
        return ApiResponse.of(SuccessStatus.REVIEW_GET_OK,response);
    }

    @Operation(summary = "리뷰 요약 조회", description = "특정 장소의 리뷰 요약 정보를 조회합니다.")
    @GetMapping(value = "/summary/{placeId}")
    public ApiResponse<ReviewResponseDTO.placeReviewSummaryDTO> getReviewSummary(@PathVariable(name="placeId")Long placeId) {
        ReviewResponseDTO.placeReviewSummaryDTO response = reviewService.getPlaceReviewSummary(placeId);
        return ApiResponse.of(SuccessStatus.REVIEW_SUMMARY_GET_OK,response);
    }

    @Operation(summary = "리뷰 상세조회", description = "하나의 리뷰를 상세 조회합니다.")
    @GetMapping(value = "/{reviewId}")
    public ApiResponse<ReviewResponseDTO.ReviewDetailDTO> getReviewDetail(@PathVariable(name="reviewId")Long reviewId) {
        ReviewResponseDTO.ReviewDetailDTO response = reviewService.getReviewDetail(reviewId);
        return ApiResponse.of(SuccessStatus.REVIEW_DETAIL_GET_OK,response);
    }
}
