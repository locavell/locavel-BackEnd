package com.example.locavel.service;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.PlacesHandler;
import com.example.locavel.converter.PlaceConverter;
import com.example.locavel.domain.PlaceImg;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.enums.Category;
import com.example.locavel.domain.enums.Region;
import com.example.locavel.repository.PlaceImgRepository;
import com.example.locavel.repository.PlaceRepository;
import com.example.locavel.web.dto.MapDTO.MapResponseDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    public PlaceService(PlaceRepository placeRepository, PlaceImgRepository placeImgRepository, S3Uploader s3Uploader, WebClient.Builder webClientBuilder) {
        this.placeRepository = placeRepository;
        this.placeImgRepository = placeImgRepository;
        this.s3Uploader = s3Uploader;
        this.webClient = webClientBuilder.build();
    }


    public Optional<Places> findPlace(Long id) {
        return placeRepository.findById(id);
    }

    public Places createPlace(PlaceRequestDTO.PlaceDTO placeDTO, List<MultipartFile> placeImgUrls) {
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

        Region region = Region.fromAddress(roadAddress);
        Places place = PlaceConverter.toPlace(placeDTO, latitude, longitude, roadAddress, region);

        if(placeImgUrls != null && !placeImgUrls.isEmpty()) {
            uploadPlaceImg(placeImgUrls, place, false);
        }
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
                        throw new RuntimeException("API response is null");
                    }
                    if (!"OK".equals(response.getStatus())) {
                        throw new RuntimeException("API response status is not OK: " + response.getStatus() + " - " + response.getErrorMessage());
                    }
                    if (response.getAddresses() == null || response.getAddresses().isEmpty()) {
                        throw new RuntimeException("No addresses found in API response");
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
}
