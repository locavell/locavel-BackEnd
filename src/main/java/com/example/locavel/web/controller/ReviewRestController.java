package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.service.ReviewService;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;

    @Operation(summary = "리뷰 등록", description = "리뷰를 등록합니다.")
    @PostMapping(value = "/{placeId}", consumes = "multipart/form-data")
    public ApiResponse<ReviewResponseDTO.ReviewResultDTO> createReview(
            @Valid @RequestBody ReviewRequestDTO.RevieweDTO request,
            @PathVariable(name="placeId") Long placeId) {
        ReviewResponseDTO.ReviewResultDTO response = reviewService.createReview(placeId, request);
        return ApiResponse.onSuccess(response);
    }
}
