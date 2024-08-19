package com.example.locavel;

import com.example.locavel.domain.Region;
import com.example.locavel.domain.Term;
import com.example.locavel.repository.RegionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//더미 데이터 생성용
@Configuration
public class MakeInitData {

    @Bean // 지역별 엔티티 생성
    public CommandLineRunner loadRegionData(RegionRepository regionRepository) {
        return args -> {
            Region region1 = Region.builder()
                    .name("서울특별시 강남구")
                    .build();
            Region region2 = Region.builder()
                    .name("서울특별시 강동구")
                    .build();
            Region region3 = Region.builder()
                    .name("서울특별시 강북구")
                    .build();
            Region region4 = Region.builder()
                    .name("서울특별시 강서구")
                    .build();
            Region region5 = Region.builder()
                    .name("서울특별시 관악구")
                    .build();
            Region region6 = Region.builder()
                    .name("서울특별시 광진구")
                    .build();
            Region region7 = Region.builder()
                    .name("서울특별시 구로구")
                    .build();
            Region region8 = Region.builder()
                    .name("서울특별시 금천구")
                    .build();
            Region region9 = Region.builder()
                    .name("서울특별시 노원구")
                    .build();
            Region region10 = Region.builder()
                    .name("서울특별시 도봉구")
                    .build();
            Region region11 = Region.builder()
                    .name("서울특별시 동대문구")
                    .build();
            Region region12 = Region.builder()
                    .name("서울특별시 동작구")
                    .build();
            Region region13 = Region.builder()
                    .name("서울특별시 마포구")
                    .build();
            Region region14 = Region.builder()
                    .name("서울특별시 서대문구")
                    .build();
            Region region15 = Region.builder()
                    .name("서울특별시 서초구")
                    .build();
            Region region16 = Region.builder()
                    .name("서울특별시 성동구")
                    .build();
            Region region17 = Region.builder()
                    .name("서울특별시 성북구")
                    .build();
            Region region18 = Region.builder()
                    .name("서울특별시 송파구")
                    .build();
            Region region19 = Region.builder()
                    .name("서울특별시 양천구")
                    .build();
            Region region20 = Region.builder()
                    .name("서울특별시 영등포구")
                    .build();
            Region region21 = Region.builder()
                    .name("서울특별시 용산구")
                    .build();
            Region region22 = Region.builder()
                    .name("서울특별시 은평구")
                    .build();
            Region region23 = Region.builder()
                    .name("서울특별시 종로구")
                    .build();
            Region region24 = Region.builder()
                    .name("서울특별시 중구")
                    .build();
            Region region25 = Region.builder()
                    .name("서울특별시 중랑구")
                    .build();
            if (regionRepository.findByName(region1.getName()) == null) {
                regionRepository.save(region1);
            }
            if (regionRepository.findByName(region2.getName()) == null) {
                regionRepository.save(region2);
            }
            if (regionRepository.findByName(region3.getName()) == null) {
                regionRepository.save(region3);
            }
            if (regionRepository.findByName(region4.getName()) == null) {
                regionRepository.save(region4);
            }
            if (regionRepository.findByName(region5.getName()) == null) {
                regionRepository.save(region5);
            }
            if (regionRepository.findByName(region6.getName()) == null) {
                regionRepository.save(region6);
            }
            if (regionRepository.findByName(region7.getName()) == null) {
                regionRepository.save(region7);
            }
            if (regionRepository.findByName(region8.getName()) == null) {
                regionRepository.save(region8);
            }
            if (regionRepository.findByName(region9.getName()) == null) {
                regionRepository.save(region9);
            }
            if (regionRepository.findByName(region10.getName()) == null) {
                regionRepository.save(region10);
            }
            if (regionRepository.findByName(region11.getName()) == null) {
                regionRepository.save(region11);
            }
            if (regionRepository.findByName(region12.getName()) == null) {
                regionRepository.save(region12);
            }
            if (regionRepository.findByName(region13.getName()) == null) {
                regionRepository.save(region13);
            }
            if (regionRepository.findByName(region14.getName()) == null) {
                regionRepository.save(region14);
            }
            if (regionRepository.findByName(region15.getName()) == null) {
                regionRepository.save(region15);
            }
            if (regionRepository.findByName(region16.getName()) == null) {
                regionRepository.save(region16);
            }
            if (regionRepository.findByName(region17.getName()) == null) {
                regionRepository.save(region17);
            }
            if (regionRepository.findByName(region18.getName()) == null) {
                regionRepository.save(region18);
            }
            if (regionRepository.findByName(region19.getName()) == null) {
                regionRepository.save(region19);
            }
            if (regionRepository.findByName(region20.getName()) == null) {
                regionRepository.save(region20);
            }
            if (regionRepository.findByName(region21.getName()) == null) {
                regionRepository.save(region21);
            }
            if (regionRepository.findByName(region22.getName()) == null) {
                regionRepository.save(region22);
            }
            if (regionRepository.findByName(region23.getName()) == null) {
                regionRepository.save(region23);
            }
            if (regionRepository.findByName(region24.getName()) == null) {
                regionRepository.save(region24);
            }
            if (regionRepository.findByName(region25.getName()) == null) {
                regionRepository.save(region25);
            }
        };
    }
}
