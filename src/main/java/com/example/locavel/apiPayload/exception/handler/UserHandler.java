package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class UserHandler extends GeneralException {
    public UserHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
