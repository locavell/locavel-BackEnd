package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class FollowHandler extends GeneralException {
    public FollowHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}