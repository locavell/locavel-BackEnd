package com.example.locavel.domain;

import com.example.locavel.domain.common.BaseEntity;
import com.example.locavel.domain.enums.Category;
import com.example.locavel.domain.enums.Region;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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

    @Enumerated(EnumType.STRING)
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
}
