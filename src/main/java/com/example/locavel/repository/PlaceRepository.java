package com.example.locavel.repository;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlaceRepository extends JpaRepository<Places, Long> {
    Places findByAddress(String address);

    @Query("SELECT p FROM Places p WHERE p.latitude BETWEEN :swLat AND :neLat AND p.longitude BETWEEN :swLng AND :neLng")
    List<Places> findPlacesInRange(
            @Param("swLat") double swLat,
            @Param("swLng") double swLng,
            @Param("neLat") double neLat,
            @Param("neLng") double neLng);

    List<Places> findByCategory(Category category);

    @Query(value = "SELECT *, " +
            "(6371 * acos(cos(radians(:latitude)) * cos(radians(latitude)) * " +
            "cos(radians(longitude) - radians(:longitude)) + sin(radians(:latitude)) * " +
            "sin(radians(latitude)))) AS distance " +
            "FROM Places " +
            "HAVING distance < :radius " +
            "ORDER BY distance", nativeQuery = true)
    List<Places> findNearbyPlaces(@Param("latitude")double latitude,
                                  @Param("longitude")double longitude,
                                  double radius);
}
