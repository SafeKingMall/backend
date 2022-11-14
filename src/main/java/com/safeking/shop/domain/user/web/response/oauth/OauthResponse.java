package com.safeking.shop.domain.user.web.response.oauth;

import com.safeking.shop.domain.user.web.response.signup.Data;
import com.safeking.shop.global.Error;
import lombok.Builder;

@lombok.Data
@Builder
public class OauthResponse {

    public static final String SUCCESS_MESSAGE = "성공적으로 완료되었습니다.";

    private int code;
    private String message;
    private Data data;
    private Error error;

}
