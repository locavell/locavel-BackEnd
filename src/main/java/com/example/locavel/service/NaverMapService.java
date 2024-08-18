package com.example.locavel.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class NaverMapService {
    @Value("https://naveropenapi.apigw.ntruss.com/map-reversegeocode/v2/gc")
    private String apiUrl;

    @Value("mdk50annar")
    private String clientId;

    @Value("nw1x5b1mNpas7lSf9sdwF7e9xuL350bHTuAonX5R")
    private String clientSecret;

    public String getRoadNameAddress(double latitude, double longitude) {
        RestTemplate restTemplate = new RestTemplate();
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(apiUrl)
                .queryParam("coords", longitude + "," + latitude)  // 좌표 순서 확인
                .queryParam("orders", "roadaddr")  // 도로명 주소를 요청
                .queryParam("output", "json");

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-NCP-APIGW-API-KEY-ID", clientId);
        headers.set("X-NCP-APIGW-API-KEY", clientSecret);

        // HttpEntity 생성
        HttpEntity<String> entity = new HttpEntity<>(headers);

        // API 요청 및 응답 처리
        ResponseEntity<String> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                String.class
        );
        return parseAddressFromResponse(response.getBody());
    }

    private String parseAddressFromResponse(String responseBody) {
        // JSON 파싱을 통해 도로명 주소를 추출하는 로직 구현
        // 예시: Jackson이나 Gson 등을 이용해 JSON 파싱
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode addressNode = root.path("results").path(0).path("region").path("area2").path("name");
            return addressNode.asText();
        } catch (Exception e) {
            e.printStackTrace();
            return "Address parsing failed";
        }
    }
}
