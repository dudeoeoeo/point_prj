package com.kei.reviewservice.business.user.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDetailDto {

    private String userId;
    private String email;
}
