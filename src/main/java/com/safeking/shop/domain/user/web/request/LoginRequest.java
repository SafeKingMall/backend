package com.safeking.shop.domain.user.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginRequest {

    private String loginId;
    private String password;
}
