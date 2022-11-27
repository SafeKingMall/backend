package com.safeking.shop.global.response;

import lombok.Data;

@Data
public class ResponseError {
    private String message;
    private int code;

    public void setResponseError(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
