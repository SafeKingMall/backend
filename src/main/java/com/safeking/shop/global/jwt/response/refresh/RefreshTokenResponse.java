package com.safeking.shop.global.jwt.response.refresh;


import com.safeking.shop.global.Error;
import lombok.Builder;

@lombok.Data
@Builder
public class RefreshTokenResponse {

    public static final String SUCCESS_MESSAGE = "Refresh Token 발행이 성공적으로 되었습니다.";

    private int code;
    private String message;
    private Data data;
    private Error error;

}
