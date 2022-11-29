package com.safeking.shop.global.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
public class ResponseDto {
    private int code;
    private String message;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("error")
    private ResponseError error;

    @Builder
    public void setResponse(int code, String message, Object data, ResponseError error) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.error = error;
    }
}
