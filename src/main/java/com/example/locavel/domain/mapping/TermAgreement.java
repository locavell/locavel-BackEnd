package com.example.locavel.domain.mapping;

import com.example.locavel.domain.Term;
import com.example.locavel.domain.User;
import com.example.locavel.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class TermAgreement extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TINYINT(1)")
    private boolean agree;//0: 비동의, 1: 동의

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "term_id")
    private Term term;

    public void setUser(User user){
        if(this.user != null){//이미 존재한다면
            user.getTermAgreementList().remove(this);
        }
        this.user = user;
        user.getTermAgreementList().add(this);
    }
}
