package com.safeking.shop.domain.order.web.controller.exception;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.global.Error;
import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static com.safeking.shop.domain.order.constant.OrderConst.*;
import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class OrderExceptionController {

    /**
     * 주문 에러 처리
     */
    @ExceptionHandler(OrderException.class)
    public ResponseEntity<Error> orderExHandler(OrderException e) {

        log.error("[OrderException] ", e);

        Error error = new Error(extractedErrorCode(e), e.getMessage());

        return new ResponseEntity<>(error, FORBIDDEN);
    }

    private int extractedErrorCode(OrderException e) {
        if(e.getMessage().equals(ORDER_CANCEL_FAIL)) {
            return 2001;
        } else if(e.getMessage().equals(ORDER_CANCEL_DELIVERY_DONE)) {
            return 2002;
        } else if(e.getMessage().equals(ORDER_MODIFY_FAIL)) {
            return 2003;
        } else if(e.getMessage().equals(ORDER_MODIFY_DELIVERY_DONE)) {
            return 2004;
        } else if(e.getMessage().equals(ORDER_LIST_FIND_FAIL)) {
            return 2005;
        } else if (e.getMessage().equals(ADMIN_ORDER_DETAIL_MODIFY_FAIL)) {
            return 2006;
        }

        return 2000;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Error> methodNotAllowedExHandler(HttpRequestMethodNotSupportedException e) {

        log.error("[HttpRequestMethodNotSupportedException] ", e);

        Error error = new Error(1, e.getMessage());

        return new ResponseEntity<>(error, BAD_REQUEST);
    }
}
