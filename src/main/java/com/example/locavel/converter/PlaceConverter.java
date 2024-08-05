package com.example.locavel.converter;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.enums.Category;
import com.example.locavel.domain.enums.Region;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;

import java.time.LocalDateTime;
import java.util.List;

public class PlaceConverter {

    public static PlaceResponseDTO.PlacePreViewDTO toPlacePreViewDTO(Places places) {
        return PlaceResponseDTO.PlacePreViewDTO.builder()
                .name(places.getName())
                .build();
    }
    public static PlaceResponseDTO.PlacePreViewListDTO toPlacePreViewListDTO(List<Places> placesList){
        return null;
    }

    public static PlaceResponseDTO.PlaceDetailDTO toPlaceDetailDTO(Places places) {
        return PlaceResponseDTO.PlaceDetailDTO.builder()
                .name(places.getName())
                .placeId(places.getId())
                .address(places.getRegion().getAddress())   // 수정 필요
                .generalRating(places.getRating())
                .longitude(places.getLongitude())
                .latitude(places.getLatitude())
//                .travelerRating()
                .build();
    }

    public static PlaceResponseDTO.PlaceResultDTO toPlaceResultDTO(Places places) {
        return PlaceResponseDTO.PlaceResultDTO.builder()
                .placeId(places.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Places toPlace(PlaceRequestDTO.PlaceDTO request, Double latitude, Double longitude, String roadAddress, Region region){

        Category category = null;

        switch (request.getCategory()){
            case "spot":
                category = Category.spot;
                break;
            case "food":
                category = Category.food;
                break;
            case "activity":
                category = Category.activity;
                break;
        }

        return Places.builder()
                .name(request.getName())
                .category(category)
                .rating(request.getRating())
                .telephoneNumber(request.getTelephoneNumber())
                .address(roadAddress)
                .region(region)
                .latitude(String.valueOf(latitude))
                .longitude(String.valueOf(longitude))
                .build();
    }
}
