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
        String location = user.getLocation();
        String placeAddress = place.getAddress();
        System.out.println("location = " + location);
        System.out.println("placeAddress = " + placeAddress);
        if (placeAddress.contains(location)) {
            return NO;
        }
        else return YES;
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
