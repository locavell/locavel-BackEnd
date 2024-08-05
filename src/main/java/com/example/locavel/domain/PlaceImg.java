package com.example.locavel.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor
public class PlaceImg {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    String imgUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "places_id")
    private Places places;
}
