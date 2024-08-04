package com.example.locavel.web.dto.PlaceDTO;

import lombok.Getter;

public class PlaceRequestDTO {

    @Getter
    public static class PlaceDTO {
        String name;
        String address;
        String category;
        String telephoneNumber;
        Float rating;
    }
}
