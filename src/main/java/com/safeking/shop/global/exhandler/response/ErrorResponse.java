package com.safeking.shop.global.exhandler.response;

import com.safeking.shop.global.Error;
import lombok.AllArgsConstructor;
import lombok.Builder;


@lombok.Data
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private int code;
    private String message;
    private Data data;
    private Error error;

}
