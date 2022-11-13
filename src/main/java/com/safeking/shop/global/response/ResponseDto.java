package com.safeking.shop.global.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseDto {
    private int code;
    private String message;
    @JsonProperty("data")
    private Object data;
    @JsonProperty("error")
    private ResponseError error;
}
