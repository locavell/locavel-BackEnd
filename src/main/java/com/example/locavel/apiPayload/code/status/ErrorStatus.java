package com.example.locavel.apiPayload.code.status;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // 가장 일반적인 응답
    _INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    _UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    _FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // S3 응답
    DUPLICATE_IMAGE(HttpStatus.BAD_REQUEST, "FILE4001", "중복된 파일입니다."),
    NO_IMAGE_EXIST(HttpStatus.BAD_REQUEST, "FILE4002", "파일이 존재하지 않습니다."),
    FAIL_DELETE(HttpStatus.INTERNAL_SERVER_ERROR, "FILE5001", "서버 에러, 파일 삭제에 실패했습니다."),
    FAIL_UPLOAD(HttpStatus.INTERNAL_SERVER_ERROR, "FILE5002", "서버 에러, 파일 업로드에 실패했습니다."),
    NOT_IMAGE_EXTENSION(HttpStatus.INTERNAL_SERVER_ERROR, "FILE4003", "파일 확장자명이 아닙니다."),

    //리뷰
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND,"REVIEW4001", "리뷰가 없습니다."),
    RATING_NOT_EXIST(HttpStatus.BAD_REQUEST, "REVIEW4002","총점은 필수 입니다."),
    RATING_NOT_VALID(HttpStatus.BAD_REQUEST,"REVIEW4003", "총점은 0~5점이어야 합니다."),

    //장소
    PLACE_NOT_FOUND(HttpStatus.NOT_FOUND, "PLACE4001", "장소가 없습니다."),;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build()
                ;
    }
}
