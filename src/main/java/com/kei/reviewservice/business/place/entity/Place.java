package com.kei.reviewservice.business.place.entity;

import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "place")
@Data
public class Place extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "place_id", unique = true)
    private String placeId;

    private String subject;

    private String content;

}
