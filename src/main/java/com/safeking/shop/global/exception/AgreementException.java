package com.safeking.shop.global.exception;

public class AgreementException extends RuntimeException{
    public AgreementException() {
        super();
    }

    public AgreementException(String message) {
        super(message);
    }

    public AgreementException(String message, Throwable cause) {
        super(message, cause);
    }

    public AgreementException(Throwable cause) {
        super(cause);
    }

    protected AgreementException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
