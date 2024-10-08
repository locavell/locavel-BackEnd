package com.example.locavel.service;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.PlacesHandler;
import com.example.locavel.converter.PlaceConverter;
import com.example.locavel.domain.*;
import com.example.locavel.domain.enums.Category;
import com.example.locavel.domain.enums.Traveler;
import com.example.locavel.repository.*;
import com.example.locavel.service.userService.UserCommandService;
import com.example.locavel.web.dto.MapDTO.MapResponseDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;
    private final PlaceImgRepository placeImgRepository;
    private final S3Uploader s3Uploader;
    private final WebClient webClient;
    private final RegionService regionService;
    private final WishListRepository wishListRepository;
    private final ReviewRepository reviewRepository;
    private final UserRegionRepository userRegionRepository;
    private final UserRepository userRepository;
    @Autowired
    private UserCommandService userCommandService; //등급 업데이트를 위해 추가

    @Autowired
    public PlaceService(PlaceRepository placeRepository,
                        PlaceImgRepository placeImgRepository,
                        S3Uploader s3Uploader,
                        WebClient.Builder webClientBuilder,
                        RegionService regionService,
                        WishListRepository wishListRepository,
                        ReviewRepository reviewRepository,
                        UserRegionRepository userRegionRepository,
                        UserRepository userRepository) {
        this.placeRepository = placeRepository;
        this.placeImgRepository = placeImgRepository;
        this.s3Uploader = s3Uploader;
        this.webClient = webClientBuilder.build();
        this.regionService = regionService;
        this.wishListRepository = wishListRepository;
        this.reviewRepository = reviewRepository;
        this.userRegionRepository = userRegionRepository;
        this.userRepository = userRepository;
    }


    public Optional<Places> findPlace(Long id) {
        return placeRepository.findById(id);
    }

    public Places createPlace(PlaceRequestDTO.PlaceDTO placeDTO, List<MultipartFile> placeImgUrls, User user) {
        MapResponseDTO response = getCoordinatesFromAddress(placeDTO.getAddress()).block();
        if (response == null) {
            throw new RuntimeException("Failed to get coordinates from address");
        }
        double longitude = Double.parseDouble(response.getAddresses().get(0).getX());
        double latitude = Double.parseDouble(response.getAddresses().get(0).getY());
        String roadAddress = response.getAddresses().get(0).getRoadAddress(); // 도로명주소 가져오기
        if(placeRepository.findByAddress(roadAddress) != null){
            throw new PlacesHandler(ErrorStatus.PLACE_ALREADY_EXIST);
        }

        Region region = regionService.findRegion(roadAddress);
        Places place = PlaceConverter.toPlace(placeDTO, latitude, longitude, roadAddress, region, user);

        if(placeImgUrls != null && !placeImgUrls.isEmpty()) {
            uploadPlaceImg(placeImgUrls, place, false);
        }
        user.setReviewCountPlus();
        userRepository.save(user);
        return placeRepository.save(place);

    }

    private Mono<MapResponseDTO> getCoordinatesFromAddress(String address) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("query", address)
                        .build())
                .retrieve()
                .bodyToMono(MapResponseDTO.class)
                .doOnNext(response -> {
                    // 응답 확인을 위한 로그 추가
                    System.out.println("Received response: " + response);
                })
                .map(response -> {
                    if (response == null) {
                        throw new PlacesHandler(ErrorStatus.ADDRESS_NOT_VALID);
                    }
                    if (!"OK".equals(response.getStatus())) {
                        throw new PlacesHandler(ErrorStatus.ADDRESS_NOT_VALID);
                    }
                    if (response.getAddresses() == null || response.getAddresses().isEmpty()) {
                        throw new PlacesHandler(ErrorStatus.ADDRESS_NOT_VALID);
                    }
                    return response;
                });
    }

    public void uploadPlaceImg(List<MultipartFile> placeImg, Places places, boolean update) {
        if (update) {
            List<PlaceImg> placeImgList = placeImgRepository.findAllByPlaces(places);
            for(PlaceImg imgUrl : placeImgList) {
                placeImgRepository.delete(imgUrl);
                s3Uploader.deleteFile(imgUrl.getImgUrl());
            }
        }
        List<String> imgUrls = s3Uploader.saveFiles(placeImg);
        for(String imgUrl : imgUrls) {
            placeImgRepository.save(PlaceConverter.toPlaceImg(places, imgUrl));
        }
    }

    public List<Places> getNearbyMarkers(float swLat, float swLng, float neLat, float neLng) {
        List<Places> places = placeRepository.findPlacesInRange(swLat, swLng, neLat, neLng);
        return places;
    }

    public List<Places> getFilterPlaces(String category) {
        Category cat = Category.valueOf(category.toLowerCase());
        return placeRepository.findByCategory(cat);
    }

    public List<Places> recommendNearbyPlaces(double latitude, double longitude, double radius) {
        return placeRepository.findNearbyPlaces(latitude, longitude, radius);
    }

    public List<Places> searchPlaces(String keyword) {
        return placeRepository.searchByKeyword(keyword);
    }
    public void addWishList(User user, Long placeId) {
        Places place = placeRepository.findById(placeId)
                .orElseThrow(()->new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND));
        WishList wishListCheck = wishListRepository.findByUserAndPlace(user,place);
        if(wishListCheck == null) {
            WishList wishList = PlaceConverter.toWishList(user, place);
            wishListRepository.save(wishList);
        }
        else {
            throw new PlacesHandler(ErrorStatus.WISHLIST_ALREADY_ADDED);
        }
    }
    public void deleteWishList(User user, Long placeId) {
        Places place = placeRepository.findById(placeId)
                .orElseThrow(()->new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND));
        WishList wishList = wishListRepository.findByUserAndPlace(user,place);
        if(wishList != null) {
            wishListRepository.delete(wishList);
        }
        else {
            throw new PlacesHandler(ErrorStatus.WISHLIST_NOT_FOUND);
        }
    }
    public PlaceResponseDTO.PlacePreviewListDTO getWishPlaceList(User user, String category, String region, Integer page) {
        Page<Places> wishPlaceList = null;
        Category cat = Category.valueOf(category.toLowerCase());
        Region userRegion = regionService.findRegion(user.getLocation());
        List<Long> interestRegionIdList = userRegionRepository.findUserRegionIdByUser(user);
        Long regionId = userRegion.getId();
        if(region.equals("my")) {
            wishPlaceList = wishListRepository.findAllPlacesByUserAndCategoryInUserRegion(user, cat, regionId, PageRequest.of(page,10));
        }
        else if(region.equals("interest")) {
            wishPlaceList = wishListRepository.findAllPlacesByUserAndCategoryInInterest(user, cat, interestRegionIdList, PageRequest.of(page,10));
        }
        else if(region.equals("etc")) {
            wishPlaceList = wishListRepository.findAllPlacesByUserAndCategoryInEct(user, cat,  regionId, interestRegionIdList, PageRequest.of(page, 10));
        }
        return PlaceConverter.toPlacePreviewListDTO(wishPlaceList);
    }
    public PlaceResponseDTO.PlacePreviewListDTO getUserVisitPlaceList(User user, Integer page) {
        Page<Places> visitPlaceList = reviewRepository.findAllPlaceByUser(user,PageRequest.of(page,10));
        return PlaceConverter.toPlacePreviewListDTO(visitPlaceList);
    }
    public void setReview(Places place) { //리뷰정보 업데이트
        Long placeId = place.getId();
        Float totalRating;
        Float travelerRating;
        Float generalRating;
        Long totalCount = reviewRepository.countAllByPlace(place);
        if(totalCount > 0L) {totalRating = reviewRepository.getAvgRatingByPlace(placeId);}
        else {totalRating = 0f;}
        Long travelerCount = reviewRepository.countAllByPlaceAndTraveler(place, Traveler.YES);
        if(travelerCount > 0L) {travelerRating = reviewRepository.getAvgRatingByPlaceAndTraveler(placeId, Traveler.YES);}
        else {travelerRating = 0f;}
        Long generalCount = reviewRepository.countAllByPlaceAndTraveler(place, Traveler.NO);
        if(generalCount > 0L) {generalRating = reviewRepository.getAvgRatingByPlaceAndTraveler(placeId, Traveler.NO);}
        else {generalRating = 0f;}
        place.setRating(totalRating);
        place.setReviewCount(totalCount);
        place.setTravelerRating(travelerRating);
        place.setTravelerReviewCount(travelerCount);
        place.setGeneralRating(generalRating);
        place.setGeneralReviewCount(generalCount);
    }
    public PlaceResponseDTO.PlacePreviewDTO setPlaceImg(PlaceResponseDTO.PlacePreviewDTO wishPlaceDTO){
        Places place = placeRepository.findById(wishPlaceDTO.getPlaceId())
                .orElseThrow(()->new PlacesHandler(ErrorStatus.PLACE_NOT_FOUND));
        String imgUrl = placeImgRepository.findPlaceImgByPlaces(place);
        wishPlaceDTO.setPlaceImg(imgUrl);
        return wishPlaceDTO;
    }
}
