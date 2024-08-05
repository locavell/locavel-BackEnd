package com.example.locavel.web.dto.PlaceDTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PlaceResponseDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacePreViewListDTO{
        List<PlacePreViewDTO> placeList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlacePreViewDTO{
        String name;
        String address;
        Float rating;
        Integer reviewCount;
        String category;
        // img 사진
//        String longitude; //경도
//        String latitude; //위도
        LocalDate createdAt;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceDetailDTO{
        Long placeId;
        String name;
        String address;
        String longitude; //경도
        String latitude; //위도
        Float generalRating;
        Float travelerRating;
        // img reviewImage
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceResultDTO{
        Long placeId;
        LocalDateTime createdAt;
    }

}
