package com.example.locavel.web.dto.MapDTO;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MapResponseDTO {
    private String status;
    private String errorMessage;
    private Meta meta;
    private List<Address> addresses;

    @Override
    public String toString() {
        return "MapResponseDTO{" +
                "status='" + status + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", meta=" + meta +
                ", addresses=" + addresses +
                '}';
    }

    @Getter
    @Setter
    public static class Meta {
        private int totalCount;
        private int page;
        private int count;
    }

    @Getter
    @Setter
    public static class Address {
        private String roadAddress;
        private String jibunAddress;
        private String englishAddress;
        private String x; // 경도
        private String y; // 위도
        private double distance;
        private List<AddressElement> addressElements;
    }

    @Getter
    @Setter
    public static class AddressElement {
        private List<String> types;
        private String longName;
        private String shortName;
        private String code;
    }
}
