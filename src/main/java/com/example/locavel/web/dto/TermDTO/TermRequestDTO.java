package com.example.locavel.web.dto.TermDTO;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

public class TermRequestDTO {

    @Getter
    public static class TermAgreeDTO{

        @NotNull
        Long user_id;

        @NotNull
        List<Long> agreeTermIds;

        @NotNull
        List<Long> disagreeTermIds;
    }
}
