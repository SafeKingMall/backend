package com.safeking.shop.domain.user.web.response.signup;

import com.safeking.shop.global.Error;
import lombok.Builder;
import lombok.Getter;

@lombok.Data
@Builder
public class SignUpResponse {

    public static final String SUCCESS_MESSAGE = "회원 가입이 성공적으로 완료되었습니다.";

    private int code;
    private String message;
    private Data data;
    private Error error;


}