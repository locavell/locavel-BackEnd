package com.example.locavel.web.controller;


import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.domain.User;
import com.example.locavel.service.UserRegionService;
import com.example.locavel.service.userService.UserCommandService;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import com.example.locavel.web.dto.UserRegionDTO.UserRegionResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class UserRegionRestController {
    private final UserRegionService userRegionService;
    private final UserCommandService userCommandService;

    @PostMapping("/api/users/interest-area")
    @Operation(summary = "관심 지역 등록 API", description = "관심 지역을 등록하는 API 입니다. query string 으로 구 이름을 주세요")
    public ApiResponse<UserRegionResponseDTO.UserRegionResultDTO> addInterestArea(HttpServletRequest httpServletRequest, String district) {
        User user = userCommandService.getUser(httpServletRequest);
        UserRegionResponseDTO.UserRegionResultDTO interestArea = userRegionService.createInterestArea(user.getId(), district);
        return ApiResponse.onSuccess(interestArea);
    }

    @DeleteMapping("/api/users/interest-area/{regionId}")
    @Operation(summary = "관심 지역 삭제 API", description = "관심 지역을 삭제하는 API 입니다.")
    public ApiResponse<UserRegionResponseDTO.UserRegionResultDTO> deleteInterestArea(HttpServletRequest httpServletRequest, @PathVariable Long regionId) {
        User user = userCommandService.getUser(httpServletRequest);
        UserRegionResponseDTO.UserRegionResultDTO userRegionResultDTO = userRegionService.deleteInterestArea(user.getId(), regionId);
        return ApiResponse.onSuccess(userRegionResultDTO);
    }

    @GetMapping("/api/users/interest-area/{regionId}/places")
    @Operation(summary = "관심 지역에 속한 장소 목록 조회 API", description = "관심 지역에 속한 장소를 조회하는 API 입니다.")
    public ApiResponse<List<PlaceResponseDTO.PlaceDetailDTO>> getPlaces(HttpServletRequest httpServletRequest, @PathVariable Long regionId) {
        User user = userCommandService.getUser(httpServletRequest);
        List<PlaceResponseDTO.PlaceDetailDTO> places = userRegionService.getPlaces(user.getId(), regionId);
        return ApiResponse.onSuccess(places);
    }

    @GetMapping("/api/users/interest-area")
    @Operation(summary = "관심 지역 목록 조회 API", description = "관심 지역 목록 조회하는 API 입니다.")
    public ApiResponse<List<UserRegionResponseDTO.UserRegionDetailsDTO>> getRegions(HttpServletRequest httpServletRequest) {
        User user = userCommandService.getUser(httpServletRequest);
        List<UserRegionResponseDTO.UserRegionDetailsDTO> regions = userRegionService.getRegions(user.getId());
        return ApiResponse.onSuccess(regions);
    }
}
