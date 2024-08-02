package com.example.locavel.service;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.ReviewsHandler;
import com.example.locavel.converter.ReviewConverter;
import com.example.locavel.domain.Places;
import com.example.locavel.domain.ReviewImg;
import com.example.locavel.domain.Reviews;
import com.example.locavel.domain.User;
import com.example.locavel.domain.enums.Traveler;
import com.example.locavel.repository.PlaceRepository;
import com.example.locavel.repository.ReviewImgRepository;
import com.example.locavel.repository.ReviewRepository;
import com.example.locavel.repository.UserRepository;
import com.example.locavel.web.dto.ReviewDTO.ReviewRequestDTO;
import com.example.locavel.web.dto.ReviewDTO.ReviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final S3Uploader s3Uploader;
    public ReviewResponseDTO.ReviewResultDTO createReview(Long placeId, ReviewRequestDTO.RevieweDTO request, List<MultipartFile> reviewImgUrls) {
        //TODO : JWT 토큰 추가 후 변경
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(()->new RuntimeException("해당하는 유저가 없습니다."));
        Places place = placeRepository.findById(placeId)
                .orElseThrow(()->new ReviewsHandler(ErrorStatus.PLACE_NOT_FOUND));
        Traveler traveler = Traveler.of(place, user);
        Reviews review = ReviewConverter.toReviews(user, traveler, place, request);
        Reviews savedReview = reviewRepository.save(review);
        if(reviewImgUrls != null && !reviewImgUrls.isEmpty()) {
            uploadReviewImg(reviewImgUrls, savedReview, false);
        }
        return ReviewConverter.toReviewResultDTO(savedReview);
    }

    public void uploadReviewImg(List<MultipartFile> reviewImg, Reviews reviews, boolean update) {
        if (update) {
            List<ReviewImg> reviewImgList = reviewImgRepository.findAllByReviews(reviews);
            for(ReviewImg imgUrl : reviewImgList) {
                reviewImgRepository.delete(imgUrl);
                s3Uploader.deleteFile(imgUrl.getImgUrl());
            }
        }
        List<String> imgUrls = s3Uploader.saveFiles(reviewImg);
        for(String imgUrl : imgUrls) {
            reviewImgRepository.save(ReviewConverter.toReviewImg(reviews, imgUrl));
        }
    }
}
