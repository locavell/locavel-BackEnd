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
                    .name("강남구")
                    .build();
            Region region2 = Region.builder()
                    .name("강동구")
                    .build();
            Region region3 = Region.builder()
                    .name("강북구")
                    .build();
            Region region4 = Region.builder()
                    .name("강서구")
                    .build();
            Region region5 = Region.builder()
                    .name("관악구")
                    .build();
            Region region6 = Region.builder()
                    .name("광진구")
                    .build();
            Region region7 = Region.builder()
                    .name("구로구")
                    .build();
            Region region8 = Region.builder()
                    .name("금천구")
                    .build();
            Region region9 = Region.builder()
                    .name("노원구")
                    .build();
            Region region10 = Region.builder()
                    .name("도봉구")
                    .build();
            Region region11 = Region.builder()
                    .name("동대문구")
                    .build();
            Region region12 = Region.builder()
                    .name("동작구")
                    .build();
            Region region13 = Region.builder()
                    .name("마포구")
                    .build();
            Region region14 = Region.builder()
                    .name("서대문구")
                    .build();
            Region region15 = Region.builder()
                    .name("서초구")
                    .build();
            Region region16 = Region.builder()
                    .name("성동구")
                    .build();
            Region region17 = Region.builder()
                    .name("성북구")
                    .build();
            Region region18 = Region.builder()
                    .name("송파구")
                    .build();
            Region region19 = Region.builder()
                    .name("양천구")
                    .build();
            Region region20 = Region.builder()
                    .name("영등포구")
                    .build();
            Region region21 = Region.builder()
                    .name("용산구")
                    .build();
            Region region22 = Region.builder()
                    .name("은평구")
                    .build();
            Region region23 = Region.builder()
                    .name("종로구")
                    .build();
            Region region24 = Region.builder()
                    .name("중구")
                    .build();
            Region region25 = Region.builder()
                    .name("중랑구")
                    .build();

            regionRepository.save(region1);
            regionRepository.save(region2);
            regionRepository.save(region3);
            regionRepository.save(region4);
            regionRepository.save(region5);
            regionRepository.save(region6);
            regionRepository.save(region7);
            regionRepository.save(region8);
            regionRepository.save(region9);
            regionRepository.save(region10);
            regionRepository.save(region11);
            regionRepository.save(region12);
            regionRepository.save(region13);
            regionRepository.save(region14);
            regionRepository.save(region15);
            regionRepository.save(region16);
            regionRepository.save(region17);
            regionRepository.save(region18);
            regionRepository.save(region19);
            regionRepository.save(region20);
            regionRepository.save(region21);
            regionRepository.save(region22);
            regionRepository.save(region23);
            regionRepository.save(region24);
            regionRepository.save(region25);
        };
    }
}
