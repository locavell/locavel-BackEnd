package com.example.locavel.repository;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.Reviews;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Traveler;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Reviews, Long>, JpaSpecificationExecutor<Reviews> {
    Page<Reviews> findAllByUser(User user, PageRequest pageRequest);
    Long countAllByPlaceAndTraveler(Places place, Traveler traveler);
    Long countAllByPlace(Places place);

    @Query("select avg(r.rating) from Reviews r where r.place.id = :placeId")
    Float getAvgRatingByPlace(@Param("placeId")Long placeId);

    @Query("select avg(r.rating) from Reviews r where r.place.id = :placeId and r.traveler = :traveler")
    Float getAvgRatingByPlaceAndTraveler(@Param("placeId")Long placeId, @Param("traveler")Traveler traveler);
}
