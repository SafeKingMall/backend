package com.safeking.shop.domain.payment.web.controller.exception;

import com.safeking.shop.domain.exception.PaymentException;
import com.safeking.shop.global.Error;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.safeking.shop.global.exhandler.erroconst.ErrorConst.PAYMENT_EX_CODE;

@Slf4j
@RestControllerAdvice
public class PaymentExceptionController {

    /**
     * 결제 에러 처리
     */
    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Error> paymentExHandler(PaymentException e) {
        log.error("[PaymentException] ", e);

        return new ResponseEntity<>(new Error(PAYMENT_EX_CODE, e.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
