package com.safeking.shop.domain.user.web.request.signuprequest;

import com.safeking.shop.domain.user.domain.service.dto.AuthenticationInfoDto;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.validator.constraints.Length;
import org.springframework.context.annotation.Lazy;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class AuthenticationInfo {

    @Length(max = 50)
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String name;

    @Pattern(regexp = "^(19[0-9][0-9]|20\\d{2})(0[0-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])$",message = "ex) 19971202")
    @NotEmpty(message = "빈 문자는 안됩니다.")
    private String birth;

    @NotNull
    @Pattern(regexp = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$", message = "10 ~ 11 자리의 숫자만 입력 가능합니다.")
    private String phoneNumber;

    public AuthenticationInfoDto toServiceDto(){
        return AuthenticationInfoDto.builder()
                .name(name)
                .birth(birth)
                .phoneNumber(phoneNumber)
                .build();
    }


}
