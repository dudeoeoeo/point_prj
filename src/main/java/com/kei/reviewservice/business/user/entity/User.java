package com.kei.reviewservice.business.user.entity;

import com.kei.reviewservice.business.user.dto.request.SignUpReq;
import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Table(name = "users")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String encryptPassword;

    public static User createUser(SignUpReq req) {
        return User.builder()
                .userId(UUID.randomUUID().toString())
                .name(req.getName())
                .email(req.getEmail())
                .encryptPassword(req.getEncryptPassword())
                .build();
    }
}
