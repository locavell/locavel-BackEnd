package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Category;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Places extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "region_id")
    private Region region;

    private double longitude; //경도

    private double latitude; //위도

    private String address;

    @Column(nullable = false)
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private Category category;

    @Column(nullable = false)
    private Float rating;

    private String telephoneNumber;

    public void setRegion(Region region) {
        this.region = region;
        region.addPlaces(this);
    }

    @OneToMany(mappedBy = "place")
    private List<Reviews> reviewList = new ArrayList<>();
    @Builder.Default
    private Float generalRating = 0f; //현지인 총점
    @Builder.Default
    private Float travelerRating = 0f; //여행객 총점
    @Builder.Default
    private Long reviewCount = 0L;
    @Builder.Default
    private Long generalReviewCount = 0L;
    @Builder.Default
    private Long travelerReviewCount = 0L;
    @OneToMany(mappedBy = "place", cascade = CascadeType.ALL)
    private List<WishList> wishLists = new ArrayList<>();
}
