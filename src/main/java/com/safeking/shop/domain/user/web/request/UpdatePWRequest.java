package com.safeking.shop.domain.user.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePWRequest {
    @NotBlank
    private String previousPassword;
    @NotBlank
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&])[A-Za-z[0-9]$@$!%*#?&]{8,20}$"
            ,message = "비밀번호는 특수문자, 영문, 숫자 조합 (8~10 자리)이어야 합니다.")
    private String password;
}
