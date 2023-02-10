package com.kei.reviewservice.business.user.service;

import com.kei.reviewservice.business.point.service.PointService;
import com.kei.reviewservice.business.user.dto.request.SignUpReq;
import com.kei.reviewservice.business.user.dto.response.UserDetailDto;
import com.kei.reviewservice.business.user.entity.User;
import com.kei.reviewservice.business.user.entity.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private PointService pointService;
    private BCryptPasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PointService pointService,
                           BCryptPasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.pointService = pointService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public void createUser(SignUpReq req) {
        req.setEncryptPassword(passwordEncoder.encode(req.getPassword()));
        final User user = User.createUser(req);

        final User save = userRepository.save(user);

        pointService.createPoint(save);
    }

    @Override
    public UserDetailDto getUserDetail(String email) {
        final User user = findByEmail(email);

        return UserDetailDto.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .build();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final User user = findByEmail(email);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getEncryptPassword(),
                true,
                true,
                true,
                true,
                new ArrayList<>()
        );
    }

    private User findByEmail(String email) {
         return userRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException(email)
        );
    }
}
