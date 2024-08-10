package com.example.locavel.web.dto.UserRegionDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class UserRegionResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegionResultDTO{
        Long userRegionId;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserRegionDetailsDTO{
        Long userRegionId;
        String regionName;
        LocalDateTime createdAt;
    }
}
