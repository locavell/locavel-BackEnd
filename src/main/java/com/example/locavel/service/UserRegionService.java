package com.example.locavel.service;

import com.example.locavel.converter.UserRegionConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.Region;
import com.example.locavel.domain.User;
import com.example.locavel.domain.mapping.UserRegion;
import com.example.locavel.repository.RegionRepository;
import com.example.locavel.repository.UserRegionRepository;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import com.example.locavel.web.dto.UserRegionDTO.UserRegionResponseDTO;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class UserRegionService {
    private final UserRegionRepository userRegionRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    public UserRegionResponseDTO.UserRegionResultDTO createInterestArea(Long userId, String distinct) {
        User user = userRepository.findById(userId).orElseThrow();
        Region region = regionRepository.findByName(distinct);
        UserRegion userRegion = UserRegion.builder()
                .region(region)
                .build();
        userRegion.setUser(user);
        UserRegion newUserRegion = userRegionRepository.save(userRegion);
        return UserRegionConverter.toUserRegionResultDTO(newUserRegion);
    }

    public UserRegionResponseDTO.UserRegionResultDTO deleteInterestArea(Long userRegionId) {
        UserRegion deleteUserRegion = userRegionRepository.findById(userRegionId).orElseThrow();
        userRegionRepository.delete(deleteUserRegion);
        return UserRegionConverter.toUserRegionResultDTO(deleteUserRegion);
    }

    public List<PlaceResponseDTO.PlaceDetailDTO> getPlaces(Long userRegionId) {
        UserRegion userRegion = userRegionRepository.findById(userRegionId).orElseThrow();
        List<Places> placesList = userRegion.getRegion().getPlacesList();
        List<PlaceResponseDTO.PlaceDetailDTO> collect = placesList.stream().map(places ->
                PlaceResponseDTO.PlaceDetailDTO.builder()
                        .name(places.getName())
                        .placeId(places.getId())
                        .address(places.getAddress())   // 수정 필요
                        .generalRating(places.getRating())
                        .longitude(places.getLongitude())
                        .latitude(places.getLatitude())
        //              .travelerRating()
                        .build()).collect(Collectors.toList());
        return collect;
    }

    public List<UserRegionResponseDTO.UserRegionDetailsDTO> getRegions(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        List<UserRegion> userRegionList = user.getUserRegionList();
        List<UserRegionResponseDTO.UserRegionDetailsDTO> collect = userRegionList.stream().map(userRegion ->
                UserRegionResponseDTO.UserRegionDetailsDTO.builder()
                        .userRegionId(userRegion.getId())
                        .regionName(userRegion.getRegion().getName())
                        .createdAt(LocalDateTime.now())
                        .build()).collect(Collectors.toList());
        return collect;
    }

}
