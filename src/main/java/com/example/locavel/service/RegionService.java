package com.example.locavel.service;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.RegionHandler;
import com.example.locavel.domain.Region;
import com.example.locavel.repository.RegionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
@RequiredArgsConstructor
public class RegionService {
    private final RegionRepository regionRepository;
    public Region findRegion(String address) {
        String district = extractDistrict(address);
        if (district.equals("없음")) {
            throw new RegionHandler(ErrorStatus.REGION_NOT_FOUND);
        }
        System.out.println(district);
        return regionRepository.findByName(district);
    }

    public static String extractDistrict(String address) {
        // 서울특별시 주소에서 구를 추출하는 정규 표현식
        Pattern pattern = Pattern.compile("\\b[가-힣]+구\\b");
        Matcher matcher = pattern.matcher(address);

        // 첫 번째 일치 항목 반환
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "없음";
        }
    }
}
