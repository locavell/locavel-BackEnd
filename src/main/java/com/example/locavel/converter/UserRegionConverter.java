package com.example.locavel.converter;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.mapping.UserRegion;
import com.example.locavel.web.dto.PlaceDTO.PlaceResponseDTO;
import com.example.locavel.web.dto.UserRegionDTO.UserRegionResponseDTO;

import java.time.LocalDateTime;

public class UserRegionConverter {
    public static UserRegionResponseDTO.UserRegionResultDTO toUserRegionResultDTO(UserRegion userRegion) {
        return UserRegionResponseDTO.UserRegionResultDTO.builder()
                .userRegionId(userRegion.getId())
                .createdAt(LocalDateTime.now())
                .build();
    }
}
