package com.kei.reviewservice.business.user.dto.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@ToString
public class SignUpReq {

    @NotEmpty(message = "이름을 입력해주세요.")
    @Size(min = 2, message = "이름은 최소 2글자 이상 입력해주세요.")
    private String name;

    @NotEmpty(message = "이메일을 입력해주세요.")
    @Email
    private String email;

    @NotEmpty(message = "비밀번호를 입력해주세요.")
    @Size(min = 8, message = "패스워드는 최소 8자 이상 입력해주세요.")
    private String password;

    private String encryptPassword;
}
