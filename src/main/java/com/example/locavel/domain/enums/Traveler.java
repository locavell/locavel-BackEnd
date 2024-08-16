package com.example.locavel.domain.enums;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.User;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Traveler {
    YES("여행객"),
    NO("현지인");

    private final String viewName;

    // 유저의 현지인/여행객 판단
    public static Traveler of(Places place, User user) {
        Long userRegion = user.getMy_area().getId();
        Long placeRegion = place.getRegion().getId();
        if (userRegion == placeRegion || userRegion.equals(placeRegion)) {
            return NO; // 현지인
        }
        else return YES; //여행객
    }
    @JsonValue
    public String getViewName() {
        return viewName;
    }
    @JsonCreator
    public static Traveler toTraveler(String value) {
        if (value == null) {
            return null;
        }
        else {
            for (Traveler traveler : Traveler.values()) {
                if (traveler.viewName.equals(value)) {
                    return traveler;
                }
            }
            throw new IllegalArgumentException("Invalid traveler: " + value);
        }
    }
}
