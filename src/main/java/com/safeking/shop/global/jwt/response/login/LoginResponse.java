package com.safeking.shop.global.jwt.response.login;

import com.safeking.shop.global.Error;
import lombok.Builder;


@Builder
@lombok.Data
public class LoginResponse {
    public static final String SUCCESS_MESSAGE = "로그인이 성공적으로 되었습니다.";

    private int code;
    private String message;
    private Data data;
    private Error error;
}
