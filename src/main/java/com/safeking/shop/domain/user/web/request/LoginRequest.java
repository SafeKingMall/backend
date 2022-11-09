package com.safeking.shop.domain.user.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;

@Getter
@AllArgsConstructor
public class LoginRequest {

    @NotEmpty
    private String loginId;
    private String password;
}
