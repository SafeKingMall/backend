package com.safeking.shop.domain.user.domain.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class AuthenticationInfoDto {

    private String name;
    private String birth;
    private String phoneNumber;
    private String smsCode;

}
