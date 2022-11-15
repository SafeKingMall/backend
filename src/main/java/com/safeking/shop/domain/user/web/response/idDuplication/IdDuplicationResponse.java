package com.safeking.shop.domain.user.web.response.idDuplication;

import com.safeking.shop.global.Error;
import lombok.Builder;


@lombok.Data
@Builder
public class IdDuplicationResponse {

    public static final String SUCCESS_MESSAGE = "ID가 중복되지 않습니다.";
    public static final String FAIL_MESSAGE = "ID가 중복됩니다.";

    private int code;
    private String message;
    private Data data;
    private Error error;

}
