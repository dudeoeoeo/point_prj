package com.kei.reviewservice.business.user.entity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Optional<User> findByUserIdAndDeleteYn(String userId, Boolean deleteYn);
}
