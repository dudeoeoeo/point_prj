package com.kei.reviewservice.business.user.controller;

import com.kei.reviewservice.business.user.dto.request.SignUpReq;
import com.kei.reviewservice.business.user.dto.response.UserDetailDto;
import com.kei.reviewservice.business.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity createUser(@Valid @RequestBody SignUpReq req) {
        userService.createUser(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }
}
