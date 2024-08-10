package com.example.locavel.apiPayload.exception.handler;

import com.example.locavel.apiPayload.code.BaseErrorCode;
import com.example.locavel.apiPayload.exception.GeneralException;

public class RegionHandler extends GeneralException {
    public RegionHandler(BaseErrorCode baseErrorCode) {super(baseErrorCode);}
}
