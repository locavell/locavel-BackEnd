package com.example.locavel.repository;

import com.example.locavel.domain.PlaceImg;
import com.example.locavel.domain.Places;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlaceImgRepository extends JpaRepository<PlaceImg, Long> {
    List<PlaceImg> findAllByPlaces(Places places);

}
