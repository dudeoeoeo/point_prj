package com.kei.reviewservice.business.point.entity;

import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "point_log")
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PointLog extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "point_log_id", unique = true)
    private String pointLogId;

    @Column(name = "review_id")
    private String reviewId;

    @Column(name = "contentYn")
    private Boolean contentYn;

    @Column(name = "photoYn")
    private Boolean photoYn;

    @Column(name = "point")
    private int point;
}
