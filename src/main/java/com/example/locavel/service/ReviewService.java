package com.example.locavel.service;

import com.example.locavel.converter.ReviewConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.Reviews;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Traveler;
import com.example.locavel.repository.PlaceRepository;
import com.example.locavel.repository.ReviewRepository;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    public ReviewResponseDTO.ReviewResultDTO createReview(Long placeId, ReviewRequestDTO.RevieweDTO request) {
        //TODO : JWT 토큰 추가 후 변경
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()->new RuntimeException("해당하는 유저가 없습니다."));
        Places place = placeRepository.findById(placeId)
                .orElseThrow(()->new RuntimeException("해당하는 장소가 없습니다."));
        Traveler traveler = Traveler.of(place, user);
        Reviews review = ReviewConverter.toReviews(user, traveler, place, request);
        Reviews savedReview = reviewRepository.save(review);
        return ReviewConverter.toReviewResultDTO(savedReview);
    }
}
