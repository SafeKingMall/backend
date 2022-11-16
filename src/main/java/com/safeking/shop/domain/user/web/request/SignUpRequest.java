package com.safeking.shop.domain.user.web.request;

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
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$"
            ,message = "아이디는 숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자' 허용\n" +
            " (특수문자는 정의된 특수문자만 사용 가능)")
    private String username;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$"
            ,message = "비밀번호는 특수문자, 영문, 숫자 조합 (8~10 자리)이어야 합니다.")
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