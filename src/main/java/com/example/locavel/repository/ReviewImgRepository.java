package com.example.locavel.repository;

import com.example.locavel.domain.ReviewImg;
import com.example.locavel.domain.Reviews;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewImgRepository extends JpaRepository<ReviewImg, Long> {
    List<ReviewImg> findAllByReviews(Reviews reviews);
}
