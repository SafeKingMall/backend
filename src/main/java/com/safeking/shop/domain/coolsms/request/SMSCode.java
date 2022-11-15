package com.safeking.shop.domain.coolsms.request;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class SMSCode {
    @NotEmpty
    private String code;

}
