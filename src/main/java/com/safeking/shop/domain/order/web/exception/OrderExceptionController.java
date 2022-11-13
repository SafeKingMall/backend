package com.safeking.shop.domain.order.web.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.global.response.ResponseDto;
import com.safeking.shop.global.response.ResponseError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class OrderExceptionController {

    /**
     * 주문 취소 에러 처리
     * @param e
     * @return
     */
    @ExceptionHandler(OrderException.class)
    public ResponseDto orderCancel(OrderException e) {

        ResponseDto responseDto = new ResponseDto();
        ResponseError responseError = new ResponseError();

        if(e.getMessage().contains("배송 중이거나 배송완료된")) {
            responseError.setCode(2001);
        }
        responseError.setCode(2000);
        responseError.setMessage(e.getMessage());

        responseDto.setCode(HTTPResponse.SC_FORBIDDEN);
        responseDto.setMessage(OrderConst.ORDER_CANCEL_FAIL);
        responseDto.setData(new ObjectMapper());
        responseDto.setError(responseError);

        return responseDto;
    }
}
