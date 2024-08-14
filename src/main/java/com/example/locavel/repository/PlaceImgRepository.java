package com.example.locavel.repository;

import com.example.locavel.domain.PlaceImg;
import com.example.locavel.domain.Places;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PlaceImgRepository extends JpaRepository<PlaceImg, Long> {
    List<PlaceImg> findAllByPlaces(Places places);
    @Query("select pi.imgUrl from PlaceImg pi where pi.places = :places order by pi.id desc limit 1")
    String findPlaceImgByPlaces(@Param("places") Places places);
}
