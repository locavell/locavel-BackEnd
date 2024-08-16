package com.example.locavel.apiPayload.code.status;

import com.example.locavel.apiPayload.code.BaseCode;
import com.example.locavel.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    //일반적인 응답
    _OK(HttpStatus.OK, "COMMON200", "성공입니다."),

    //유저
    USER_TERM_AGREED(HttpStatus.OK,"USER2000", "약관동의가 정상적으로 등록되었습니다."),
    USER_PROFILE_UPDATED(HttpStatus.OK, "USER2002", "프로필이 정상적으로 수정되었습니다."),
    USER_UPDATED(HttpStatus.OK, "USER2002", "유저 정보가 정상적으로 수정되었습니다."),
    USER_DELETED(HttpStatus.OK,"USER2003", "유저가 정상적으로 삭제되었습니다."),
    USER_FOUND(HttpStatus.OK,"USER2005","유저를 정상적으로 조회했습니다."),

    //유저 등급
    GRADE_GET_OK(HttpStatus.OK, "GRADE2004", "유저 등급 조회를 정상적으로 조회했습니다."),

    //리뷰
    REVIEW_CREATE_OK(HttpStatus.OK,"REVIEW2001", "리뷰가 정상적으로 등록되었습니다."),
    REVIEW_UPDATE_OK(HttpStatus.OK,"REVIEW2002", "리뷰가 정상적으로 수정되었습니다."),
    REVIEW_DELETE_OK(HttpStatus.OK,"REVIEW2003", "리뷰가 정상적으로 삭제되었습니다."),
    REVIEW_GET_OK(HttpStatus.OK,"REVIEW2004","리뷰 목록을 조회했습니다."),
    REVIEW_SUMMARY_GET_OK(HttpStatus.OK,"REVIEW2005", "리뷰 요약을 조회했습니다."),
    REVIEW_DETAIL_GET_OK(HttpStatus.OK,"REVIEW2006", "리뷰를 상세조회했습니다."),

    // 장소
    PLACE_CREATE_OK(HttpStatus.OK, "PLACE2001", "장소가 정상적으로 등록되었습니다."),
    PLACE_GET_OK(HttpStatus.OK, "PLACE2004", "장소를 조회했습니다."),
    PLACE_LIST_GET_OK(HttpStatus.OK, "PLACE2004", "장소 목록을 조회했습니다."),
    PLACE_MARKER_GET_OK(HttpStatus.OK, "PLACE2004", "장소 마커 정보를 조회했습니다."),

    //팔로우
    FOLLOW_OK(HttpStatus.OK, "FOLLOW2001", "팔로우가 완료되었습니다."),
    FOLLOW_DELETE_OK(HttpStatus.OK,"FOLLOW2002","팔로우 취소가 완료되었습니다."),
    FOLLOW_GET_OK(HttpStatus.OK,"FOLLOW2003","팔로우 목록을 조회했습니다."),

    //위시리스트
    WISHLIST_ADD_OK(HttpStatus.OK,"WISHLIST2001","위시리스트에 저장되었습니다."),
    WISHLIST_DELETE_OK(HttpStatus.OK,"WISHLIST2002","위시리스트에서 삭제되었습니다."),
    WISHLIST_GET_OK(HttpStatus.OK,"WISHLIST2003","위시리스트를 조회했습니다."),

    //마이페이지
    MYPAGE_PROFILE_GET_OK(HttpStatus.OK,"MYPAGE2001","유저 프로필을 조회했습니다."),
    MYPAGE_VISIT_GET_OK(HttpStatus.OK,"MYPAGE2002","유저가 방문한 곳을 조회했습니다."),
    MYPATE_CALENDAR_GET_OK(HttpStatus.OK,"MYPAGE2003","유저가 방문한 날짜를 조회했습니다"),
    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build()
                ;
    }

}
