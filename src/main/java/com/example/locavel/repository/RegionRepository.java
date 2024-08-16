package com.example.locavel.repository;

import com.example.locavel.domain.Region;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long> {

    public Region findByName(String name);
}
