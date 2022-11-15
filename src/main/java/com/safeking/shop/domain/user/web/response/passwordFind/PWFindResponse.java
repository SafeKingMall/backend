package com.safeking.shop.domain.user.web.response.passwordFind;

import com.safeking.shop.global.Error;
import lombok.Builder;

@Builder
@lombok.Data
public class PWFindResponse {
    public static final String SUCCESS_MESSAGE = "비밀번호가 재 발급 되었습니다. 본인 명의의 휴대전화를 확인해주세요";

    private int code;
    private String message;
    private Data data;
    private Error error;

}
