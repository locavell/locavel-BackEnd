package com.example.locavel.service.termService;

import com.example.locavel.apiPayload.code.status.ErrorStatus;
import com.example.locavel.apiPayload.exception.handler.TermHandler;
import com.example.locavel.converter.TermConverter;
import com.example.locavel.domain.Term;
import com.example.locavel.domain.User;
import com.example.locavel.domain.mapping.TermAgreement;
import com.example.locavel.repository.TermAgreementRepository;
import com.example.locavel.repository.TermRepository;
import com.example.locavel.service.userService.UserCommandService;
import com.example.locavel.web.dto.TermDTO.TermRequestDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TermServiceImpl implements TermService{
    private final UserCommandService userCommandService;
    private final TermRepository termRepository;
    private final TermAgreementRepository termAgreementRepository;

    @Override
    @Transactional
    public List<TermAgreement> agree(TermRequestDTO.TermAgreeDTO termAgreeDTO){
        User user = userCommandService.findUser(termAgreeDTO.getUser_id());
        List<Term> agreeTermsList = termAgreeDTO.getAgreeTermIds().stream()
                .map(termId -> {
                    return termRepository.findById(termId).orElseThrow(() -> new TermHandler(ErrorStatus.TERMS_NOT_FOUND));
                }).collect(Collectors.toList());
        List<Term> disagreeTermsList = termAgreeDTO.getDisagreeTermIds().stream()
                .map(termId -> {
                    return termRepository.findById(termId).orElseThrow(() -> new TermHandler(ErrorStatus.TERMS_NOT_FOUND));
                }).collect(Collectors.toList());
        List<TermAgreement> termsAgreementList = TermConverter.toAgreeTermsAgreement(agreeTermsList);
        List<TermAgreement> termsDisagreementList = TermConverter.toDisagreeTermsAgreement(disagreeTermsList);
        List<TermAgreement> allTermsAgreementList = new ArrayList<>(termsAgreementList);
        allTermsAgreementList.addAll(termsDisagreementList);
        allTermsAgreementList.forEach(termsAgreement -> {
            termsAgreement.setUser(user);
        });
        termAgreementRepository.saveAll(allTermsAgreementList);

        return allTermsAgreementList;
    }
}
