package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.ReviewsHandler;
import com.example.locavel.converter.PlaceConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.Reviews;
import com.example.locavel.service.PlaceService;
import com.example.locavel.service.ReviewService;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PlaceRestController {

    private final PlaceService placeService;
    private final ReviewService reviewService;

    @GetMapping("/api/places/{placeId}")
    @Operation(summary = "특정 장소 상세 조회 API", description = "특정 장소를 상세 조회하는 API입니다. query String으로 place 번호를 주세요")
    public ApiResponse<PlaceResponseDTO.PlaceDetailDTO> getPlace(@PathVariable Long placeId) {
        Optional<Places> place = placeService.findPlace(placeId);
        List<String> reviewImgList = null;
        if (place.isPresent()) {
            reviewImgList = reviewService.getReviewImagesByPlace(place.get());
        }

        return ApiResponse.onSuccess(PlaceConverter.toPlaceDetailDTO(place.orElse(null), reviewImgList));
    }

    @PostMapping(value = "/api/places", consumes = "multipart/form-data")
    @Operation(summary = "장소 등록 API", description = "새로운 장소를 등록하는 API입니다. 장소명, 별점은 필수로 입력해야 합니다.")
    public ApiResponse<PlaceResponseDTO.PlaceResultDTO> createPlace(@Valid @RequestPart PlaceRequestDTO.PlaceDTO placeDTO,
                                                                    @RequestPart(required = false) List<MultipartFile> placeImgUrls) {
        if(placeDTO.getRating() > 5 || placeDTO.getRating() <0) {
            throw new ReviewsHandler(ErrorStatus.RATING_NOT_VALID);
        }
        Places place = placeService.createPlace(placeDTO, placeImgUrls);
        return ApiResponse.onSuccess(PlaceConverter.toPlaceResultDTO(place));
    }

    @GetMapping("/api/places/map")
    @Operation(summary = "내 주변 장소 조회(마커) API", description = "지도뷰에서 내 주변 장소를 조회하는 API입니다.")
    public ApiResponse<List<PlaceResponseDTO.NearbyMarkerDTO>> getNearbyMarker(@RequestParam float swLat,    // 남서쪽 구석 위도
                                                                         @RequestParam float swLng,    // 납서쪽 구석 경도
                                                                         @RequestParam float neLat,    // 북동쪽 구석 위도
                                                                         @RequestParam float neLng){   // 북동쪽 구석 경도

        List<Places> places = placeService.getNearbyMarkers(swLat, swLng, neLat, neLng);
        return ApiResponse.onSuccess(PlaceConverter.toNearbyMarkerDTO(places));
    }

    @GetMapping("/api/places/filters")
    @Operation(summary = "스팟, 푸드, 액티비티 필터 조회(마커) API", description = "지도뷰에서 스팟, 푸드, 액티비티 필터로 장소 위치를 조회하는 API입니다.")
    public ApiResponse<PlaceResponseDTO.FilterMarkerListDTO> getFilterMarker(@RequestParam String category){
        List<Places> places = placeService.getFilterPlaces(category);
        return ApiResponse.onSuccess(PlaceConverter.toFilterMarkerListDTO(places));
    }

    @GetMapping("/api/places/list")
    public ApiResponse<PlaceResponseDTO.FilterPlaceListDTO> getFilterPlaceList(@RequestParam String category) {
        List<Places> places = placeService.getFilterPlaces(category);

        List<List<Reviews>> reviewsLists = places.stream()
                .map(reviewService::getReviewsByPlace)
                .collect(Collectors.toList());

        List<List<String>> reviewImgLists = places.stream()
                .map(reviewService::getReviewImagesByPlace)
                .collect(Collectors.toList());

        return ApiResponse.onSuccess(PlaceConverter.toFilterPlaceListDTO(places, reviewsLists, reviewImgLists));
    }
}
