package com.safeking.shop.domain.coolsms.response;

import com.safeking.shop.global.Error;
import lombok.Builder;

@Builder
@lombok.Data
public class SMSResponse {

    public static final String SUCCESS_MESSAGE = "성공적으로 완료되었습니다.";

    private int code;
    private String message;
    private Data data;
    private Error error;

}
