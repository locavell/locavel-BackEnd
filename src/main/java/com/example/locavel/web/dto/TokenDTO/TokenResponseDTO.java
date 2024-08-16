package com.example.locavel.web.dto.TokenDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TokenResponseDTO {

    private String accessToken;
    private String refreshToken;
}
