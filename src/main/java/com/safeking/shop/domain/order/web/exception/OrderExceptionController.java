package com.safeking.shop.domain.order.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.controller.OrderController;
import com.safeking.shop.global.response.ResponseDto;
import com.safeking.shop.global.response.ResponseError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class OrderExceptionController {

    /**
     * 주문 취소 에러 처리
     * @param e
     * @return
     */
    @ExceptionHandler(OrderException.class)
    public ResponseDto cancelOrderExHandler(OrderException e) {

        ResponseDto responseDto = new ResponseDto();
        ResponseError responseError = new ResponseError();

        responseError.setResponseError(e.getMessage(), extractedErrorCode(e));
        responseDto.setResponse(HTTPResponse.SC_FORBIDDEN, OrderConst.ORDER_CANCEL_FAIL, new ObjectMapper(), responseError);

        return responseDto;
    }

    private int extractedErrorCode(OrderException e) {
        if(e.getMessage().equals(OrderConst.ORDER_CANCEL_DELIVERY_DONE)) {
            return 2001;
        } else if(e.getMessage().equals(OrderConst.ORDER_MODIFY_FAIL)) {
            return 2002;
        } else if(e.getMessage().equals(OrderConst.ORDER_MODIFY_DELIVERY_DONE)) {
            return 2003;
        }

        return 2000;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseDto methodNotAllowedExHandler(HttpRequestMethodNotSupportedException e) {
        ResponseDto responseDto = new ResponseDto();
        ResponseError responseError = new ResponseError();

        responseError.setResponseError(e.getMessage(), 1);
        responseDto.setResponse(HTTPResponse.SC_BAD_REQUEST, OrderConst.BLANK, new ObjectMapper(), responseError);

        return responseDto;
    }
}
