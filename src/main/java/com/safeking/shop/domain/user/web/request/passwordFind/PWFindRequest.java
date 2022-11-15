package com.safeking.shop.domain.user.web.request.passwordFind;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class PWFindRequest {

    @NotNull
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d~!@#$%^&*()+|=]{8,20}$"
            ,message = "아이디는 숫자', '문자' 무조건 1개 이상, '최소 8자에서 최대 20자' 허용\n" +
            " (특수문자는 정의된 특수문자만 사용 가능)")
    private String username;
}
