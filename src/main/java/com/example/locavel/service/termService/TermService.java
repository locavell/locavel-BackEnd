package com.example.locavel.service.termService;

import com.example.locavel.domain.mapping.TermAgreement;
import com.example.locavel.web.dto.TermDTO.TermRequestDTO;

import java.util.List;

public interface TermService {
    public List<TermAgreement> agree (TermRequestDTO.TermAgreeDTO termAgreeDTO);
}
