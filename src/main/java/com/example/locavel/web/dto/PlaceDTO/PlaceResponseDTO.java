package com.example.locavel.web.dto.PlaceDTO;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

public class PlaceResponseDTO {

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
        List<String> reviewImgList;
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterPlaceDTO{
        Long placeId;
        String name;
        String address;
        Float rating;
        int reviewCount;
        List<String> reviewImgList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FilterPlaceListDTO{
        List<FilterPlaceDTO> filterPlaceDTOList;
        String category;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SearchResultPlaceDTO{
        Long placeId;
        String name;
        Float rating;
        int reviewCount;
        List<String> reviewImgList;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WishPlaceListDTO {
        List<WishPlaceDTO> wishPlaceDTOList;
        Integer listSize;
        Integer totalPage;
        Long totalElements;
        Boolean isFirst;
        Boolean isLast;
    }
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Setter
    public static class WishPlaceDTO {
        Long placeId;
        String name;
        String placeImg;
        String description;
        Float totalRating; // 리뷰 총점
        Float generalRating; // 현지인 리뷰 총점
        Float travelerRating; // 여행객 리뷰 총점
    }
}
