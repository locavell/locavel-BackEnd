package com.example.locavel.repository;

import com.example.locavel.domain.mapping.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRegionRepository extends JpaRepository<UserRegion, Long> {
    public UserRegion findByUserIdAndRegionId(Long userId, Long regionId);
}
