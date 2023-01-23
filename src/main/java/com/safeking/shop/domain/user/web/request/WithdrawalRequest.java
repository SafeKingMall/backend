package com.safeking.shop.domain.user.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class WithdrawalRequest {
    @NotBlank
    private String inputUsername;
    @NotBlank
    private String password;
}
