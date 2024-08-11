package com.example.locavel.repository;

import com.example.locavel.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FollowRepository extends JpaRepository<Follow, Long> {
    Follow findByUserIdAndFollowUserId(Long userId, Long followUserId);

    @Query("select f.followUserId from Follow f where f.user.id = :userId")
    List<Long> findAllFollowUserIdByUserId(@Param("userId") Long userId);
    @Query("select f.user.id from Follow f where f.followUserId = :followUserId")
    List<Long> findAllUserIdByFollowUserId(@Param("followUserId")Long followUserId);
}
