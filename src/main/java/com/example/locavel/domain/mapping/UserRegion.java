package com.example.locavel.domain.mapping;

import com.example.locavel.domain.Region;
import com.example.locavel.domain.User;
import com.example.locavel.domain.common.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class UserRegion extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)//지연 로딩
    @JoinColumn(name = "region_id")
    private Region region;

    public void setUser(User user) {
        this.user = user;
        user.addUserRegionList(this);
    }
}
