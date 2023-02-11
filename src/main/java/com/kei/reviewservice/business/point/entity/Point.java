package com.kei.reviewservice.business.point.entity;

import com.kei.reviewservice.business.user.entity.User;
import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "point")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Point extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_id", unique = true)
    private String pointId;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "point")
    private Long point;

    public static Point createPoint(User user) {
        return Point.builder()
                .pointId(UUID.randomUUID().toString())
                .user(user)
                .point(0L)
                .build();
    }

    public void plusPoint(int point) {
        this.point += point;
    }

    public void minusPoint(int point) {
        this.point -= point;
    }
}
