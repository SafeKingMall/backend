package com.safeking.shop.domain.order.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.domain.order.web.dto.request.modify.ModifyInfoDto;
import com.safeking.shop.global.response.ResponseData;
import com.safeking.shop.global.response.ResponseDto;
import com.safeking.shop.global.response.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class OrderController {
    private final OrderService orderService;

    /**
     * 주문 취소
     */
    @DeleteMapping("/order")
    public ResponseDto cancel(@Valid @RequestBody CancelDto cancelDto) {

        orderService.cancel(cancelDto);

        ResponseDto responseDto = new ResponseDto();
        ResponseError responseError = new ResponseError();

        responseError.setResponseError(OrderConst.BLANK, 0);
        responseDto.setResponse(HTTPResponse.SC_OK, OrderConst.ORDER_CANCEL_SUCCESS, new ObjectMapper(), responseError);

        return responseDto;
    }
    /**
     * 주문(배송) 정보 수정
     */
    @PutMapping("/order")
    public ResponseDto modify(@Valid @RequestBody ModifyInfoDto modifyInfoDto) {

        Long orderId = orderService.modifyOrder(modifyInfoDto);

        ResponseDto responseDto = new ResponseDto();
        ResponseError responseError = new ResponseError();

        responseError.setResponseError(OrderConst.BLANK, 0);
        responseDto.setResponse(HTTPResponse.SC_OK, OrderConst.ORDER_MODIFY_SUCCESS, new ObjectMapper(), responseError);

        return responseDto;
    }
}
