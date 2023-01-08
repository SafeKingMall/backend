package com.safeking.shop.domain.exception;

/**
 * 결제 예외
 */
public class PaymentException extends RuntimeException {
    public PaymentException(String message) {
        super(message);
    }
}
