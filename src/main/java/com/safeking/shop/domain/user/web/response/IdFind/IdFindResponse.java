package com.safeking.shop.domain.user.web.response.IdFind;

import com.safeking.shop.global.Error;
import lombok.Builder;

@Builder
@lombok.Data
public class IdFindResponse {

    public static final String SUCCESS_MESSAGE = "아이디 찾기 성공.";
    public static final String FAIL_MESSAGE = "아이디 찾기 실패.";

    private int code;
    private String message;
    private Data data;
    private Error error;

}
