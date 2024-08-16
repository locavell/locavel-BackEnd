package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Traveler;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class Reviews extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "place_id")
    private Places place;

    private String comment;

    @Enumerated(EnumType.STRING)
    private Traveler traveler;

    @Column(nullable = false)
    private Float rating;
    @OneToMany(mappedBy = "reviews", cascade = CascadeType.ALL)
    private List<ReviewImg> reviewImgList = new ArrayList<>();

}
