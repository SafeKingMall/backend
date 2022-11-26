package com.safeking.shop.domain.order.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.controller.OrderController;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.response.ResponseDto;
import com.safeking.shop.global.response.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class OrderExceptionController {

    /**
     * 주문 취소 에러 처리
     * @param e
     * @return
     */
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Error> cancelOrderExHandler(OrderException e) {

        log.error("[OrderException] ex", e);

        Error error = new Error();
        error.setCode(extractedErrorCode(e));
        error.setMessage(OrderConst.ORDER_CANCEL_FAIL);

        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    private int extractedErrorCode(OrderException e) {
        if(e.getMessage().equals(OrderConst.ORDER_CANCEL_FAIL)) {
            return 2001;
        } else if(e.getMessage().equals(OrderConst.ORDER_CANCEL_DELIVERY_DONE)) {
            return 2002;
        } else if(e.getMessage().equals(OrderConst.ORDER_MODIFY_FAIL)) {
            return 2003;
        } else if(e.getMessage().equals(OrderConst.ORDER_MODIFY_DELIVERY_DONE)) {
            return 2004;
        } else if(e.getMessage().equals(OrderConst.ORDER_LIST_FIND_FAIL)) {
            return 2005;
        }

        return 2000;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error> methodNotAllowedExHandler(HttpRequestMethodNotSupportedException e) {

        log.error("[HttpRequestMethodNotSupportedException] ex", e);

        Error error = new Error();
        error.setCode(1);
        error.setMessage(e.getMessage());

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
