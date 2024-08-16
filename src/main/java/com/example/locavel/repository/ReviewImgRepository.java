package com.example.locavel.repository;

import com.example.locavel.domain.ReviewImg;
import com.example.locavel.domain.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findAllByReviews(Reviews reviews);
    @Query("select r.imgUrl from ReviewImg r where r.reviews.id = :reviewId")
    List<String> findAllImgUrlByReview(@Param("reviewId")Long reviewId);
}
