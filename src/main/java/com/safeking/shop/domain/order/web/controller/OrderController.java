package com.safeking.shop.domain.order.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.oauth2.sdk.http.HTTPResponse;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.cancel.CancelDto;
import com.safeking.shop.global.response.ResponseData;
import com.safeking.shop.global.response.ResponseDto;
import com.safeking.shop.global.response.ResponseError;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class OrderController {
    private final OrderService orderService;

    @DeleteMapping("/order")
    public ResponseDto cancel(@RequestBody CancelDto cancelDto, BindingResult bindingResult) {
        log.info("CancelDtos={}", cancelDto.getCancelOrderDtos());
        orderService.cancel(cancelDto);

        ResponseDto responseDto = new ResponseDto();
        ResponseError responseError = new ResponseError();

        responseDto.setCode(HTTPResponse.SC_OK);
        responseDto.setMessage(OrderConst.ORDER_CANCEL_SUCCESS);
        responseDto.setData(new ObjectMapper());

        responseError.setCode(0);
        responseError.setMessage(OrderConst.BLANK);

        responseDto.setError(responseError);

        return responseDto;
    }
}
