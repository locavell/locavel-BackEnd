package com.example.locavel.web.dto.PlaceDTO;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PlaceResponseDTO {

//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PlacePreViewListDTO{
//        List<PlacePreViewDTO> placeList;
//        Integer listSize;
//        Integer totalPage;
//        Long totalElements;
//        Boolean isFirst;
//        Boolean isLast;
//    }
//
//    @Getter
//    @Builder
//    @NoArgsConstructor
//    @AllArgsConstructor
//    public static class PlacePreViewDTO{
//        String name;
//        String address;
//        Float rating;
//        Integer reviewCount;
//        String category;
//        // img 사진
////        String longitude; //경도
////        String latitude; //위도
//        LocalDate createdAt;
//    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PlaceDetailDTO{
        Long placeId;
        String name;
        String address;
        double longitude; //경도
        double latitude; //위도
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class NearbyMarkerDTO{
        Long placeId;
        Double latitude;    // 위도
        Double longitude;  // 경도
        String category;
        String categoryImgUrl;  // 카테고리 아이콘 이미지 url
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterMarkerDTO{
        Long placeId;
        Double latitude;    // 위도
        Double longitude;  // 경도
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterMarkerListDTO{
        List<FilterMarkerDTO> filterMarkerDTOList;
        String category;
        String categoryImgUrl;
    }
}
