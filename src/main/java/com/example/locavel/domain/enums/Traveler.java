package com.example.locavel.domain.enums;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.User;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Traveler {
    YES("여행자"),
    NO("현지인");

    private final String viewName;

    // 유저의 현지인/여행자 판단
    public static Traveler of(Places place, User user) {
        String location = user.getLocation();
        String placeAddress = place.getRegion().getAddress();
        if (placeAddress.contains(location)) {
            return NO;
        }
        else return YES;
    }
    @JsonValue
    public String getViewName() {
        return viewName;
    }
}
