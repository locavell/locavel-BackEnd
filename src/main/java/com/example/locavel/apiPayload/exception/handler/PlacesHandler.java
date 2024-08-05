package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class PlacesHandler extends GeneralException {
    public PlacesHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
