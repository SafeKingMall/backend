package com.safeking.shop.domain.user.web.request.signuprequest;

import com.safeking.shop.domain.user.domain.service.dto.CriticalItemsDto;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Getter
@Builder
public class CriticalItems {

    @NotBlank
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$"
            ,message = "아이디는 숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자' 허용\n" +
            " 아이디는 숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자' 허용")
    private String username;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$"
            ,message = "비밀번호는 특수문자, 영문, 숫자 조합 (8~10 자리)이어야 합니다.")
    private String password;
    @Email
    private String email;

    public CriticalItemsDto toServiceDto(){
        return CriticalItemsDto.builder()
                .username(username)
                .password(password)
                .email(email)
                .build();
    }

}
