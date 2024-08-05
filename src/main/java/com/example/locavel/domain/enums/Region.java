package com.example.locavel.domain.enums;

public enum Region {
    GANGNAM_GU("대한민국", "서울특별시 강남구", "127.0473", "37.5172"),
    GANGDONG_GU("대한민국", "서울특별시 강동구", "127.1464", "37.5301"),
    GANGBUK_GU("대한민국", "서울특별시 강북구", "127.0250", "37.6396"),
    GANGSEO_GU("대한민국", "서울특별시 강서구", "126.8495", "37.5658"),
    GWANAK_GU("대한민국", "서울특별시 관악구", "126.9438", "37.4784"),
    GWANGJIN_GU("대한민국", "서울특별시 광진구", "127.0857", "37.5482"),
    GURO_GU("대한민국", "서울특별시 구로구", "126.8569", "37.4955"),
    GEUMCHEON_GU("대한민국", "서울특별시 금천구", "126.9003", "37.4569"),
    NOWON_GU("대한민국", "서울특별시 노원구", "127.0771", "37.6543"),
    DOBONG_GU("대한민국", "서울특별시 도봉구", "127.0344", "37.6687"),
    DONGDAEMUN_GU("대한민국", "서울특별시 동대문구", "127.0507", "37.5743"),
    DONGJAK_GU("대한민국", "서울특별시 동작구", "126.9536", "37.5124"),
    MAPO_GU("대한민국", "서울특별시 마포구", "126.9084", "37.5663"),
    SEODAEMUN_GU("대한민국", "서울특별시 서대문구", "126.9368", "37.5794"),
    SEOCHO_GU("대한민국", "서울특별시 서초구", "127.0374", "37.4871"),
    SEONGDONG_GU("대한민국", "서울특별시 성동구", "127.0409", "37.5509"),
    SEONGBUK_GU("대한민국", "서울특별시 성북구", "127.0194", "37.5894"),
    SONGPA_GU("대한민국", "서울특별시 송파구", "127.1145", "37.5145"),
    YANGCHEON_GU("대한민국", "서울특별시 양천구", "126.8655", "37.5169"),
    YEONGDEUNGPO_GU("대한민국", "서울특별시 영등포구", "126.9104", "37.5262"),
    YONGSAN_GU("대한민국", "서울특별시 용산구", "126.9675", "37.5333"),
    EUNPYEONG_GU("대한민국", "서울특별시 은평구", "126.9318", "37.6023"),
    JONGNO_GU("대한민국", "서울특별시 종로구", "126.9780", "37.5730"),
    JUNG_GU("대한민국", "서울특별시 중구", "126.9957", "37.5636"),
    JUNGRANG_GU("대한민국", "서울특별시 중랑구", "127.0922", "37.6063"),
    ;
    // 나머지 구들도 동일하게 정의
    ;

    private final String country;
    private final String address;
    private final String longitude;
    private final String latitude;

    Region(String country, String address, String longitude, String latitude) {
        this.country = country;
        this.address = address;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public static Region fromAddress(String address) {
        for (Region region : Region.values()) {
            if (address.contains(region.address)) {
                return region;
            }
        }
        throw new IllegalArgumentException("No matching region for address: " + address);
    }
}
