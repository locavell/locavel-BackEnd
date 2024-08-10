package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class TermHandler extends GeneralException {
    public TermHandler(BaseErrorCode errorCode){
        super(errorCode);
    }
}
