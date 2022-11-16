package com.safeking.shop.domain.user.web.request;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class UpdateRequest {
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String name;
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

    public MemberUpdateDto toServiceDto(){
        return MemberUpdateDto.builder()
                .name(name)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(new Address(city,street,zipcode))
                .build();
    }

}
