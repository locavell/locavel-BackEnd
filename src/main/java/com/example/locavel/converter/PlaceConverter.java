package com.example.locavel.converter;

import com.example.locavel.domain.*;
import com.example.locavel.domain.enums.Category;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PlaceConverter {

    public static PlaceResponseDTO.PlaceDetailDTO toPlaceDetailDTO(Places places, List<String> reviewImgList) {
        return PlaceResponseDTO.PlaceDetailDTO.builder()
                .name(places.getName())
                .placeId(places.getId())
                .address(places.getAddress())   // 수정 필요
                .generalRating(places.getRating())
                .longitude(places.getLongitude())
                .latitude(places.getLatitude())
                .reviewImgList(reviewImgList)
//                .travelerRating()
                .build();
    }

    public static PlaceResponseDTO.PlaceResultDTO toPlaceResultDTO(Places places) {
        return PlaceResponseDTO.PlaceResultDTO.builder()
                .placeId(places.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static Places toPlace(PlaceRequestDTO.PlaceDTO request,
                                 Double latitude, Double longitude,
                                 String roadAddress, Region region, User user){
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

        Places places = Places.builder()
                .user(user)
                .name(request.getName())
                .category(category)
                .rating(request.getRating())
                .telephoneNumber(request.getTelephoneNumber())
                .address(roadAddress)
                .region(region)
                .latitude(latitude)
                .longitude(longitude)
                .build();
//        places.setRegion(region);
        return places;
    }

    public static PlaceImg toPlaceImg(Places places, String url){
        return PlaceImg.builder()
                .places(places)
                .imgUrl(url)
                .build();
    }

    public static List<PlaceResponseDTO.NearbyMarkerDTO> toNearbyMarkerDTO(List<Places> places) {
        return places.stream()
                .map(place -> PlaceResponseDTO.NearbyMarkerDTO.builder()
                        .placeId(place.getId())
                        .latitude(place.getLatitude())
                        .longitude(place.getLongitude())
                        .category(place.getCategory().toString())
                        .categoryImgUrl(place.getCategory().getIconUrl())
                        .build())
                .collect(Collectors.toList());
    }

    public static PlaceResponseDTO.FilterMarkerDTO toFilterMarkerDTO(Places places) {
        return PlaceResponseDTO.FilterMarkerDTO.builder()
                .placeId(places.getId())
                .latitude(places.getLatitude())
                .longitude(places.getLongitude())
                .build();
    }

    public static PlaceResponseDTO.FilterMarkerListDTO toFilterMarkerListDTO(List<Places> places) {
        List<PlaceResponseDTO.FilterMarkerDTO> filterMarkerDTOList = places.stream()
                .map(PlaceConverter::toFilterMarkerDTO)
                .collect(Collectors.toList());

        return PlaceResponseDTO.FilterMarkerListDTO.builder()
                .filterMarkerDTOList(filterMarkerDTOList)
                .category(places.get(0).getCategory().toString())
                .categoryImgUrl(places.get(0).getCategory().getIconUrl())
                .build();
    }

    public static PlaceResponseDTO.FilterPlaceDTO toFilterPlaceDTO(Places place, List<Reviews> reviewsList, List<String> reviewImgList) {
        return PlaceResponseDTO.FilterPlaceDTO.builder()
                .placeId(place.getId())
                .name(place.getName())
                .address(place.getAddress())
                .rating(place.getRating())
                .reviewCount(reviewsList.size())
                .reviewImgList(reviewImgList)
                .build();
    }

    public static PlaceResponseDTO.FilterPlaceListDTO toFilterPlaceListDTO(List<Places> places, List<List<Reviews>> reviewsLists, List<List<String>> reviewImgLists) {
        List<PlaceResponseDTO.FilterPlaceDTO> filterPlaceDTOList = places.stream()
                .map(place -> toFilterPlaceDTO(
                        place,
                        reviewsLists.get(places.indexOf(place)),
                        reviewImgLists.get(places.indexOf(place))
                ))
                .collect(Collectors.toList());

        return PlaceResponseDTO.FilterPlaceListDTO.builder()
                .filterPlaceDTOList(filterPlaceDTOList)
                .category(places.get(0).getCategory().toString())
                .build();
    }

    public static List<PlaceResponseDTO.FilterPlaceDTO> toRecommendPlace(List<Places> places, List<List<Reviews>> reviewsLists, List<List<String>> reviewImgLists) {
        return places.stream()
                .map(place -> toFilterPlaceDTO(
                        place,
                        reviewsLists.get(places.indexOf(place)),
                        reviewImgLists.get(places.indexOf(place))
                ))
                .collect(Collectors.toList());
    }

    public static PlaceResponseDTO.SearchResultPlaceDTO toSearchResultPlaceDTO(Places place) {
        List<String> reviewImgList = place.getReviewList().stream()
                .flatMap(review -> review.getReviewImgList().stream())
                .map(ReviewImg::getImgUrl)
                .collect(Collectors.toList());

        return PlaceResponseDTO.SearchResultPlaceDTO.builder()
                .placeId(place.getId())
                .name(place.getName())
                .rating(place.getRating())
                .reviewCount(place.getReviewList().size())
                .reviewImgList(reviewImgList)
                .build();
    }

    public static List<PlaceResponseDTO.SearchResultPlaceDTO> toSearchResultPlaceListDTO(List<Places> placesList) {
        return placesList.stream()
                .map(PlaceConverter::toSearchResultPlaceDTO)
                .collect(Collectors.toList());
    }
    public static WishList toWishList(User user, Places place) {
        return WishList.builder()
                .user(user)
                .place(place)
                .build();
    }
    public static PlaceResponseDTO.PlacePreviewDTO toPlacePreviewDTO(Places place) {
        return PlaceResponseDTO.PlacePreviewDTO.builder()
                .description(place.getDescription())
                .totalRating(place.getRating())
                .generalRating(place.getGeneralRating())
                .travelerRating(place.getTravelerRating())
                .name(place.getName())
                .placeId(place.getId())
                .build();
    }

    public static PlaceResponseDTO.PlacePreviewListDTO toPlacePreviewListDTO(Page<Places> placeList) {
        List<PlaceResponseDTO.PlacePreviewDTO> placePreviewDTOList = placeList.stream()
                .map(PlaceConverter::toPlacePreviewDTO).collect(Collectors.toList());
        return PlaceResponseDTO.PlacePreviewListDTO.builder()
                .PlacePreviewDTOList(placePreviewDTOList)
                .listSize(placeList.getSize())
                .isLast(placeList.isLast())
                .isFirst(placeList.isFirst())
                .totalPage(placeList.getTotalPages())
                .totalElements(placeList.getTotalElements())
                .build();
    }
}
