package com.example.locavel.web.controller;

import com.example.locavel.apiPayload.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
    @GetMapping("/health")
    public ApiResponse<String> healthCheck() {
        return ApiResponse.onSuccess("OK!!");
    }
}
