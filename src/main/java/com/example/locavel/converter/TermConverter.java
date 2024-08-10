package com.example.locavel.converter;

import com.example.locavel.domain.Term;
import com.example.locavel.domain.mapping.TermAgreement;
import com.example.locavel.web.dto.TermDTO.TermResponseDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TermConverter {
    public static List<TermAgreement> toAgreeTermsAgreement(List<Term> termsList){
        return termsList.stream()
                .map(terms ->
                        TermAgreement.builder()
                                .agree(true)
                                .term(terms)
                                .build()
                ).collect(Collectors.toList());
    }

    public static List<TermAgreement> toDisagreeTermsAgreement(List<Term> termsList){
        return termsList.stream()
                .map(terms ->
                        TermAgreement.builder()
                                .agree(false)
                                .term(terms)
                                .build()
                ).collect(Collectors.toList());
    }

    public static TermResponseDTO.TermAgreeResultDTO toTermAgreeResultDTO(List<TermAgreement> termsAgreementList) {
        List<Long> agreeTermsId = termsAgreementList.stream()
                .filter(TermAgreement::isAgree)
                .map(termsAgreement -> {
                    return termsAgreement.getTerm().getId();
                })
                .collect(Collectors.toList());
        List<Long> disagreeTermsId = termsAgreementList.stream()
                .filter(termsAgreement -> !termsAgreement.isAgree())
                .map(termsAgreement -> {
                    return termsAgreement.getTerm().getId();
                })
                .collect(Collectors.toList());
        Long memberId = termsAgreementList.get(0).getUser().getId();

        return TermResponseDTO.TermAgreeResultDTO.builder()
                .agreeTermIds(agreeTermsId)
                .disagreeTermIds(disagreeTermsId)
                .user_id(memberId)
                .build();
    }
}
