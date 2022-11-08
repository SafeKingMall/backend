package com.safeking.shop.domain.user.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {

    private String loginId;
    private String password;
    private String email;
}