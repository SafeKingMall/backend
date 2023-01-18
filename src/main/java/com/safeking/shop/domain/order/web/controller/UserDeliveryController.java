package com.safeking.shop.domain.order.web.controller;

import com.safeking.shop.domain.order.web.dto.response.user.userdelivery.DeliveryResponse;
import com.safeking.shop.domain.order.web.dto.response.user.userdelivery.UserDeliveryResponse;
import com.safeking.shop.domain.order.web.query.service.ValidationOrderService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.safeking.shop.domain.user.constant.UserDeliveryConst.DEFAULT_USER_DELIVERY_FIND_SUCCESS;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.springframework.http.HttpStatus.OK;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserDeliveryController {

    private final ModelMapper modelMapper;
    private final ValidationOrderService validationOrderService;

    @GetMapping("/delivery")
    public ResponseEntity<UserDeliveryResponse> delivery(HttpServletRequest request) {

        Member findMember = validationOrderService.validationMember(request.getHeader(AUTH_HEADER));

        DeliveryResponse dto = modelMapper.map(findMember, DeliveryResponse.class);

        UserDeliveryResponse response = new UserDeliveryResponse();
        response.setDelivery(dto);
        response.setMessage(DEFAULT_USER_DELIVERY_FIND_SUCCESS);

        return new ResponseEntity<>(response, OK);
    }
}
