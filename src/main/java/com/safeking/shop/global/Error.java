package com.safeking.shop.global;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class Error {

    private int code;
    private String message;

}
