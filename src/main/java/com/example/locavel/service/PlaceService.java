package com.example.locavel.service;

import com.example.locavel.converter.PlaceConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.enums.Region;
import com.example.locavel.repository.PlaceRepository;
import com.example.locavel.web.dto.MapDTO.MapResponseDTO;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PlaceService {
    private final PlaceRepository placeRepository;

    private final WebClient webClient;

    @Autowired
    public PlaceService(PlaceRepository placeRepository, WebClient.Builder webClientBuilder) {
        this.placeRepository = placeRepository;
        this.webClient = webClientBuilder.build();
    }


    public Optional<Places> findPlace(Long id) {
        return placeRepository.findById(id);
    }

//    public Places createPlace(PlaceRequestDTO.PlaceDTO request) {
//        Places place = PlaceConverter.toPlace(request);
//        return placeRepository.save(place);
//    }
private static final Logger logger = LoggerFactory.getLogger(PlaceService.class);

    public Places createPlace(PlaceRequestDTO.PlaceDTO placeDTO) {

        // 네이버 API를 사용하여 위도와 경도 값을 가져오기
        MapResponseDTO response = getCoordinatesFromAddress(placeDTO.getAddress()).block(); // Mono를 block하여 값을 얻기
//        logger.info("Sending request with body: {}", requestBody);
        if (response == null) {
            throw new RuntimeException("Failed to get coordinates from address");
        }
        double longitude = Double.parseDouble(response.getAddresses().get(0).getX());
        double latitude = Double.parseDouble(response.getAddresses().get(0).getY());
        String roadAddress = response.getAddresses().get(0).getRoadAddress(); // 도로명주소 가져오기

        Region region = Region.fromAddress(roadAddress);

        Places place = PlaceConverter.toPlace(placeDTO, latitude, longitude, roadAddress, region);
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
//                    logger.info("Received response: {}", response); // 로깅을 위한 코드
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
//                    throw new RuntimeException("Failed to get coordinates from address");
                });
    }

//    public Page<Places> getPlaceList(Region region, Integer page) {
//
//        Places place = placeRepository.findById(StoreId).get();
//
//        Page<Places> placePage = placeRepository.findAllByPlace(place, PageRequest.of(page, 10));
//        return placePage;
//
//    }
}
