package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class S3Handler extends GeneralException {
    public S3Handler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
