package com.safeking.shop.domain.exception;

public class LoginException extends IllegalArgumentException {
    public LoginException(String s) {
        super(s);
    }
}
