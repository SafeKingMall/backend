package com.safeking.shop.domain.coolsms.web.controller;

import com.safeking.shop.domain.coolsms.web.query.SMSService;
import com.safeking.shop.domain.coolsms.web.request.PhoneNumber;
import com.safeking.shop.domain.coolsms.web.request.SMSCode;
import com.safeking.shop.global.Error;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coolsms")
public class SMSController {

    private final SMSService smsService;

    @PostMapping
    public String sendCodeToClient(@RequestBody @Validated PhoneNumber phoneNumber) throws CoolsmsException {
        // 클라이언트에게 전달 해줄 코드를 생성
        return smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());
    }

    @PostMapping("/code")
    public ResponseEntity checkCode(@RequestBody @Validated SMSCode smsCode){

        return smsService.checkCode(smsCode.getCode(),smsCode.getClientPhoneNumber()) ?
                new ResponseEntity(HttpStatus.OK) : ResponseEntity.badRequest().body(new Error(1500, "코드가 일치하지 않습니다."));

    }




}
