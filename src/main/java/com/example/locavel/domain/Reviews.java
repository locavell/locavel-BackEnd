package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Traveler;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

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
    @ElementCollection
    @CollectionTable(name = "review_img", joinColumns = @JoinColumn(name = "reviews_id"))
    private List<String> reviewImg;

    private String comment;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Traveler traveler;

    @NotNull
    private Float rating;

}
