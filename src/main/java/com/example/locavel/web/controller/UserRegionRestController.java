package com.example.locavel.web.controller;


import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.service.UserRegionService;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import com.example.locavel.web.dto.UserRegionDTO.UserRegionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRegionRestController {
    private final UserRegionService userRegionService;

    @PostMapping("/api/users/{userId}/interest-area")
    @Operation(summary = "관심 지역 등록 API", description = "관심 지역을 등록하는 API 입니다. query string 으로 구 이름을 주세요")
    public ApiResponse<UserRegionResponseDTO.UserRegionResultDTO> addInterestArea(@PathVariable Long userId, String district) {
        UserRegionResponseDTO.UserRegionResultDTO interestArea = userRegionService.createInterestArea(userId, district);
        return ApiResponse.onSuccess(interestArea);
    }

    @DeleteMapping("/api/users/interest-area/{userRegionId}")
    @Operation(summary = "관심 지역 삭제 API", description = "관심 지역을 삭제하는 API 입니다.")
    public ApiResponse<UserRegionResponseDTO.UserRegionResultDTO> deleteInterestArea(@PathVariable Long userRegionId) {
        UserRegionResponseDTO.UserRegionResultDTO userRegionResultDTO = userRegionService.deleteInterestArea(userRegionId);
        return ApiResponse.onSuccess(userRegionResultDTO);
    }

    @GetMapping("/api/users/interest-area/{userRegionId}/places")
    @Operation(summary = "관심 지역에 속한 장소 목록 조회 API", description = "관심 지역에 속한 장소를 조회하는 API 입니다.")
    public ApiResponse<List<PlaceResponseDTO.PlaceDetailDTO>> getPlaces(@PathVariable Long userRegionId) {
        List<PlaceResponseDTO.PlaceDetailDTO> places = userRegionService.getPlaces(userRegionId);
        return ApiResponse.onSuccess(places);
    }

    @GetMapping("/api/users/{userId}/interest-area")
    @Operation(summary = "관심 지역 목록 조회 API", description = "관심 지역 목록 조회하는 API 입니다.")
    public ApiResponse<List<UserRegionResponseDTO.UserRegionDetailsDTO>> getRegions(@PathVariable Long userId) {
        List<UserRegionResponseDTO.UserRegionDetailsDTO> regions = userRegionService.getRegions(userId);
        return ApiResponse.onSuccess(regions);
    }
}
