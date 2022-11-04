package com.safeking.shop.domain.user.web.response;

public class SignUpResponse {

    private static final String SUCCESS_MESSAGE = "회원 가입이 성공적으로 완료되었습니다.";

    private String message;

    public SignUpResponse() {
        this.message = SUCCESS_MESSAGE;
    }

    public SignUpResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
