package com.kei.reviewservice.business.user.entity;

import com.kei.reviewservice.business.user.dto.request.SignUpReq;
import com.kei.reviewservice.common.constant.BaseTimeEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
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
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", unique = true, nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String encryptPassword;

    public static User createUser(SignUpReq req) {
        return User.builder()
                .id(UUID.randomUUID().toString())
                .name(req.getName())
                .email(req.getEmail())
                .encryptPassword(req.getEncryptPassword())
                .build();
    }
}
