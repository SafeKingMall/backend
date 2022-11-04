package com.safeking.shop.domain.user.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LoginResponse {

    private String token;
    private boolean additionalAuthRequired;
}
