package com.example.locavel.repository;

import com.example.locavel.domain.Places;
import com.example.locavel.domain.User;
import com.example.locavel.domain.WishList;
import com.example.locavel.domain.enums.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WishListRepository extends JpaRepository<WishList, Long> {
    WishList findByUserAndPlace(User user, Places place);
    @Query("SELECT w.place FROM WishList w WHERE w.user = :user")
    Page<Places> findAllPlacesByUser(@Param("user") User user, PageRequest pageRequest);
    @Query("SELECT w.place " +
            "FROM WishList w " +
            "WHERE w.user = :user and w.place.category = :category and w.place.region.id = :regionId")
    Page<Places> findAllPlacesByUserAndCategoryInUserRegion(@Param("user") User user, @Param("category") Category category, @Param("regionId")Long regionId, PageRequest pageRequest);
    @Query("SELECT w.place " +
            "FROM WishList w " +
            "WHERE w.user = :user and w.place.category = :category and w.place.region.id IN :regionIds")
    Page<Places> findAllPlacesByUserAndCategoryInInterest(@Param("user") User user, @Param("category") Category category, @Param("regionIds") List<Long> regionIds, PageRequest pageRequest);
    @Query("SELECT w.place " +
            "FROM WishList w " +
            "WHERE w.user = :user and w.place.category = :category " +
            "and w.place.region.id not IN :regionIds and w.place.region.id != :regionId")
    Page<Places> findAllPlacesByUserAndCategoryInEct(@Param("user") User user, @Param("category") Category category, @Param("regionId")Long regionId, @Param("regionIds") List<Long> regionIds, PageRequest pageRequest);

}