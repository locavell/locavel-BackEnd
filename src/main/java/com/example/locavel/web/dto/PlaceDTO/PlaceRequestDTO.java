package com.example.locavel.web.dto.PlaceDTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

public class PlaceRequestDTO {

    @Getter
    public static class PlaceDTO {
        @NotNull
        String name;
        String address;
        String category;
        String telephoneNumber;
        Float rating;
    }
}
