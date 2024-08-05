package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.ReviewsHandler;
import com.example.locavel.converter.PlaceConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.service.PlaceService;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class PlaceRestController {

    private final PlaceService placeService;

    public PlaceRestController(PlaceService placeService) {
        this.placeService = placeService;
    }


    @GetMapping("/api/places/{placeId}")
    @Operation(summary = "특정 장소 상세 조회 API", description = "특정 장소를 상세 조회하는 API입니다. query String으로 place 번호를 주세요")
    public ApiResponse<PlaceResponseDTO.PlaceDetailDTO> getPlace(@PathVariable Long placeId) {
        Optional<Places> place = placeService.findPlace(placeId);
        return ApiResponse.onSuccess(PlaceConverter.toPlaceDetailDTO(place.orElse(null)));
    }

    @PostMapping("/api/places")
    public ApiResponse<PlaceResponseDTO.PlaceResultDTO> createPlace(@Valid @RequestBody PlaceRequestDTO.PlaceDTO placeDTO) {
        if(placeDTO.getRating() > 5 || placeDTO.getRating() <0) {
            throw new ReviewsHandler(ErrorStatus.RATING_NOT_VALID);
        }
        Places place = placeService.createPlace(placeDTO);
        return ApiResponse.onSuccess(PlaceConverter.toPlaceResultDTO(place));
    }


//    @GetMapping("/api/places/list")
//    @Operation(summary = "지도에서 목록 버튼으로 가게 목록 조회 API", description = "현재 지도 화면의 가게 목록을 조회하는 API이며, 페이징을 포함합니다. query String 으로 page 번호를 주세요")
//    public ApiResponse<PlaceResponseDTO.PlacePreViewListDTO> getPlacesList() {
////        return placeService.getPlaceList();
//        Page<Review> temp = storeQueryService.getReviewList(storeId, page);
//        Page<Place> temp = placeService.getPlaceList()
//        return ApiResponse.onSuccess(PlaceConverter.toPlacePreViewListDTO())
//    }
}
