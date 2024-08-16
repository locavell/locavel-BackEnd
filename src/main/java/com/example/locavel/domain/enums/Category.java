package com.example.locavel.domain.enums;

import lombok.Getter;

public enum Category {
    spot("https://d2e0trgpaip524.cloudfront.net/spot.png"),
    food("https://d2e0trgpaip524.cloudfront.net/food.png"),
    activity("https://d2e0trgpaip524.cloudfront.net/activity.png");

    @Getter
    private final String iconUrl;

    Category(String iconUrl) {
        this.iconUrl = iconUrl;
    }
}