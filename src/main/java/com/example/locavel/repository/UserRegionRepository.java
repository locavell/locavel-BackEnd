package com.example.locavel.repository;

import com.example.locavel.domain.User;
import com.example.locavel.domain.mapping.UserRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRegionRepository extends JpaRepository<UserRegion, Long> {
    public UserRegion findByUserIdAndRegionId(Long userId, Long regionId);
    @Query("select ur.region.id from UserRegion ur where ur.user = :user")
    List<Long> findUserRegionIdByUser(@Param("user") User user);
}
