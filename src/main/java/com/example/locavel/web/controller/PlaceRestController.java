package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.PlacesHandler;
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

import com.example.locavel.apiPayload.code.status.SuccessStatus;


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
        }else{
            throw new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND);
        }

        return ApiResponse.of(SuccessStatus.PLACE_GET_OK, PlaceConverter.toPlaceDetailDTO(place.orElse(null), reviewImgList));
    }

    @PostMapping(value = "/api/places", consumes = "multipart/form-data")
    @Operation(summary = "장소 등록 API", description = "새로운 장소를 등록하는 API입니다. 장소명, 별점은 필수로 입력해야 합니다.")
    public ApiResponse<PlaceResponseDTO.PlaceResultDTO> createPlace(@Valid @RequestPart PlaceRequestDTO.PlaceDTO placeDTO,
                                                                    @RequestPart(required = false) List<MultipartFile> placeImgUrls) {
        if(placeDTO.getRating() > 5 || placeDTO.getRating() <0) {
            throw new ReviewsHandler(ErrorStatus.RATING_NOT_VALID);
        }
        Places place = placeService.createPlace(placeDTO, placeImgUrls);
        if(place == null) {throw new PlacesHandler(ErrorStatus.ADDRESS_NOT_VALID);}
        return ApiResponse.of(SuccessStatus.PLACE_CREATE_OK, PlaceConverter.toPlaceResultDTO(place));
    }

    @GetMapping("/api/places/map")
    @Operation(summary = "내 주변 장소 조회(마커) API", description = "지도뷰에서 내 주변 장소를 조회하는 API입니다.")
    public ApiResponse<List<PlaceResponseDTO.NearbyMarkerDTO>> getNearbyMarker(@RequestParam float swLat,    // 남서쪽 구석 위도
                                                                         @RequestParam float swLng,    // 납서쪽 구석 경도
                                                                         @RequestParam float neLat,    // 북동쪽 구석 위도
                                                                         @RequestParam float neLng){   // 북동쪽 구석 경도

        List<Places> places = placeService.getNearbyMarkers(swLat, swLng, neLat, neLng);
        return ApiResponse.of(SuccessStatus.PLACE_MARKER_GET_OK, PlaceConverter.toNearbyMarkerDTO(places));
    }

    @GetMapping("/api/places/filters")
    @Operation(summary = "스팟, 푸드, 액티비티 필터 조회(마커) API", description = "지도뷰에서 스팟, 푸드, 액티비티 필터로 장소의 마커 정보를 조회합니다.")
    public ApiResponse<PlaceResponseDTO.FilterMarkerListDTO> getFilterMarker(@RequestParam String category){
        List<Places> places = placeService.getFilterPlaces(category);
        return ApiResponse.of(SuccessStatus.PLACE_MARKER_GET_OK, PlaceConverter.toFilterMarkerListDTO(places));
    }

    @GetMapping("/api/places/list")
    @Operation(summary = "스팟, 푸드, 액티비티 필터 조회(목록 버튼) API", description = "스팟, 푸드, 액티비티 필터로 장소 목록을 조회하는 API입니다.")
    public ApiResponse<PlaceResponseDTO.FilterPlaceListDTO> getFilterPlaceList(@RequestParam String category) {
        List<Places> places = placeService.getFilterPlaces(category);

        if(places.isEmpty()){
            throw new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND);
        }

        List<List<Reviews>> reviewsLists = places.stream()
                .map(reviewService::getReviewsByPlace)
                .collect(Collectors.toList());

        List<List<String>> reviewImgLists = places.stream()
                .map(reviewService::getReviewImagesByPlace)
                .collect(Collectors.toList());

        return ApiResponse.of(SuccessStatus.PLACE_LIST_GET_OK, PlaceConverter.toFilterPlaceListDTO(places, reviewsLists, reviewImgLists));
    }

    @GetMapping("/api/places/recommend-results")
    @Operation(summary = "장소 검색 조회(내 주변 추천 장소) API", description = "검색어 입력 전 내 주변 추천 장소 목록을 조회합니다.")
    public ApiResponse<List<PlaceResponseDTO.FilterPlaceDTO>> recommendPlace(@RequestParam double latitude,  // 사용자의 위도
                                                                    @RequestParam double longitude) {   // 사용자의 경도
        double radius = 200;
        List<Places> places = placeService.recommendNearbyPlaces(latitude, longitude, radius);

        if(places.isEmpty()){
            throw new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND);
        }

        List<List<Reviews>> reviewsLists = places.stream()
                .map(reviewService::getReviewsByPlace)
                .collect(Collectors.toList());

        List<List<String>> reviewImgLists = places.stream()
                .map(reviewService::getReviewImagesByPlace)
                .collect(Collectors.toList());

        return ApiResponse.of(SuccessStatus.PLACE_LIST_GET_OK, PlaceConverter.toRecommendPlace(places, reviewsLists, reviewImgLists));
    }

    @GetMapping("/api/places/search-results")
    @Operation(summary = "장소 검색 결과 조회 API", description = "검색 결과 장소 목록을 조회합니다.")
    public ApiResponse<List<PlaceResponseDTO.SearchResultPlaceDTO>> searchPlace(@RequestParam String keyword) {
        List<Places> places = placeService.searchPlaces(keyword);
        if(places.isEmpty()){
            throw new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND);
        }
        return ApiResponse.of(SuccessStatus.PLACE_LIST_GET_OK, PlaceConverter.toSearchResultPlaceListDTO(places));
    }


}

// 지역명을 받고 해당 지역에 속하는