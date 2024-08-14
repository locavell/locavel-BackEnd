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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final PlaceRepository placeRepository;
    private final ReviewImgRepository reviewImgRepository;
    private final S3Uploader s3Uploader;
    public ReviewResponseDTO.ReviewResultDTO createReview(User user, Long placeId, ReviewRequestDTO.ReviewDTO request, List<MultipartFile> reviewImgUrls) {
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

    public ReviewResponseDTO.ReviewUpdateResultDTO updateReview(Long reviewId, ReviewRequestDTO.ReviewDTO request, List<MultipartFile> reviewImgUrls) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new ReviewsHandler(ErrorStatus.REVIEW_NOT_FOUND));
        if(request.getRating() != null) {review.setRating(request.getRating());}
        if(request.getComment() != null) {review.setComment(request.getComment());}
        if(reviewImgUrls != null) {uploadReviewImg(reviewImgUrls, review, true);}
        Reviews updatedReview = reviewRepository.save(review);
        return ReviewConverter.toReviewUpdateResultDTO(updatedReview);
    }

    public ReviewResponseDTO.ReviewResultDTO deleteReview(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new ReviewsHandler(ErrorStatus.REVIEW_NOT_FOUND));
        ReviewResponseDTO.ReviewResultDTO resultDTO = ReviewConverter.toReviewResultDTO(review);
        List<ReviewImg> reviewImgList = reviewImgRepository.findAllByReviews(review);
        for(ReviewImg imgUrl : reviewImgList) {
            s3Uploader.deleteFile(imgUrl.getImgUrl());
        }
        reviewRepository.delete(review);
        return resultDTO;
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

    public Page<Reviews> getReviewList(Long placeId, Integer page, String sortOption, Traveler traveler) {

        //필터 : 현지인/여행객
        Specification<Reviews> spec = Specification.where(null);
        spec = spec.and((root, query, cb) -> cb.equal(root.get("place").get("id"),placeId));
        if(traveler != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("traveler"), traveler));
        }

        //정렬 (기본 : 최신순)
        spec = spec.and((root, query, cb) -> {
            if (sortOption == null || sortOption.equals("최신순")) {
                query.orderBy(cb.desc(root.get("created_at")));
            } else if(sortOption.equals("오래된순")){
                query.orderBy(cb.asc(root.get("created_at")));
            }
            return cb.conjunction();
        });

        return reviewRepository.findAll(spec, PageRequest.of(page, 10));
    }

    public Page<Reviews> getMyReviewList(Long userId, Integer page) {
        //TODO: JWT 토큰 추가 후 변경
        User user = userRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("유저가 없습니다"));
        return reviewRepository.findAllByUser(user,PageRequest.of(page,10));
    }

    public ReviewResponseDTO.placeReviewSummaryDTO getPlaceReviewSummary(Long placeId) {
        Places place = placeRepository.findById(placeId)
                .orElseThrow(()->new ReviewsHandler(ErrorStatus.PLACE_NOT_FOUND));
        Float totalRating = reviewRepository.getAvgRatingByPlace(placeId);
        Long totalCount = reviewRepository.countAllByPlace(place);
        Float travelerRating = reviewRepository.getAvgRatingByPlaceAndTraveler(placeId, Traveler.YES);
        Long travelerCount = reviewRepository.countAllByPlaceAndTraveler(place,Traveler.YES);
        Float generalRating = reviewRepository.getAvgRatingByPlaceAndTraveler(placeId, Traveler.NO);
        Long generalCount = reviewRepository.countAllByPlaceAndTraveler(place, Traveler.NO);

        return ReviewConverter.toPlaceReviewSummaryDTO(totalRating, totalCount, generalRating, generalCount, travelerRating, travelerCount);
    }

    public ReviewResponseDTO.ReviewDetailDTO getReviewDetail(Long reviewId) {
        Reviews review = reviewRepository.findById(reviewId)
                .orElseThrow(()->new ReviewsHandler(ErrorStatus.REVIEW_NOT_FOUND));
        List<String> reviewImgList = reviewImgRepository.findAllImgUrlByReview(reviewId);
        return ReviewConverter.toReviewDetailDTO(review, reviewImgList);
    }

    public List<Reviews> getReviewsByPlace(Places place) {
        return reviewRepository.findAllByPlace(place);
    }

    public List<String> getReviewImagesByPlace(Places place) {
        return reviewRepository.findAllByPlace(place).stream()
                .flatMap(review -> review.getReviewImgList().stream())
                .map(ReviewImg::getImgUrl)
                .collect(Collectors.toList());
    }
}
