package com.safeking.shop.domain.user.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class UpdatePWRequest {
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$"
            ,message = "비밀번호는 특수문자, 영문, 숫자 조합 (8~10 자리)이어야 합니다.")
    private String password;
}
