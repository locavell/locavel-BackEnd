package com.example.locavel.service;

import com.example.locavel.converter.PlaceConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.repository.PlaceRepository;
import com.example.locavel.web.dto.PlaceDTO.PlaceRequestDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
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

    public Places createPlace(PlaceRequestDTO.PlaceDTO request) {
        Places place = PlaceConverter.toPlace(request);
        return placeRepository.save(place);
    }

//
//    public Places createPlace(PlaceRequestDTO.PlaceDTO placeDTO) {
//        // 네이버 API를 사용하여 위도와 경도 값을 가져오기
//        double[] coordinates = getCoordinatesFromAddress(placeDTO.getAddress()).block(); // Mono를 block하여 값을 얻기
//        if (coordinates == null) {
//            throw new RuntimeException("Failed to get coordinates from address");
//        }
//        Places place = PlaceConverter.toPlace(placeDTO, coordinates[1], coordinates[0]);
//        return placeRepository.save(place);
//    }
//
//    private Mono<double[]> getCoordinatesFromAddress(String address) {
//        String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
//
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder.path("/geocode")
//                        .queryParam("query", encodedAddress)
//                        .build())
//                .retrieve()
//                .bodyToMono(MapResponseDTO.class)
//                .map(response -> {
//                    if ("OK".equals(response.getStatus()) && response.getAddresses() != null && !response.getAddresses().isEmpty()) {
//                        double longitude = Double.parseDouble(response.getAddresses().get(0).getX());
//                        double latitude = Double.parseDouble(response.getAddresses().get(0).getY());
//                        return new double[]{longitude, latitude};
//                    }
//                    throw new RuntimeException("Failed to get coordinates from address");
//                });
//    }

//    public Page<Places> getPlaceList(Region region, Integer page) {
//
//        Places place = placeRepository.findById(StoreId).get();
//
//        Page<Places> placePage = placeRepository.findAllByPlace(place, PageRequest.of(page, 10));
//        return placePage;
//
//    }
}
