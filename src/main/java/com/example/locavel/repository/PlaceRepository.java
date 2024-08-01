package com.example.locavel.repository;

import com.example.locavel.domain.Places;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Places, Long> {
}
