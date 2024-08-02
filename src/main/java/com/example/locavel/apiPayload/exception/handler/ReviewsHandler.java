package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class ReviewsHandler extends GeneralException {
    public ReviewsHandler(BaseErrorCode errorCode){super(errorCode);}
}
