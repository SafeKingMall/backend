package com.safeking.shop.domain.user.web.request.signup;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class SignUpRequest {

    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String name;
    @NotNull
    @Pattern(regexp = "^[a-zA-Z]{1}[a-zA-Z0-9_]{4,11}$",message = "시작은 영문으로만, '_'를 제외한 특수문자 안되며 영문, 숫자, '_'으로만 이루어진 5 ~ 12자 이하")
    private String username;
    @NotNull
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\\\d)(?=.*\\\\W).{8,20}$",message = "비밀번호는 영문, 특수문자, 숫자 포함 8자 이상이어야 합니다.")
    private String password;
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String city;
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String street;
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String zipcode;


    public GeneralSingUpDto toServiceDto(){
        return GeneralSingUpDto.builder()
                .name(name)
                .username(username)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(new Address(city,street,zipcode))
                .build();
    }
}