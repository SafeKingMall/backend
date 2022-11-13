package com.safeking.shop.domain.coolsms;

import com.safeking.shop.domain.coolsms.request.SMSRequest;
import com.safeking.shop.domain.coolsms.response.Data;
import com.safeking.shop.domain.coolsms.response.SMSResponse;
import com.safeking.shop.global.Error;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/coolsms")
public class SMSController {

    private final SMSService smsService;

    @PostMapping
    public ResponseEntity<SMSResponse> sendCodeToClient(@RequestBody @Validated SMSRequest smsRequest) throws CoolsmsException {

        String code = smsService.sendCodeToClient(smsRequest.getClientPhoneNumber());

        return ResponseEntity.ok()
                .body(SMSResponse.builder()
                        .code(200)
                        .message(SMSResponse.SUCCESS_MESSAGE)
                        .data(new Data(code))
                        .error(new Error())
                        .build());
    }

    @PostMapping("/code")
    public ResponseEntity<SMSResponse> checkCode(@RequestBody SMSRequest smsRequest){

        if (smsService.checkCode(smsRequest.getCode())){
            return ResponseEntity.ok()
                    .body(SMSResponse.builder()
                            .code(200)
                            .message(SMSResponse.SUCCESS_MESSAGE)
                            .data(new Data(Data.DEFAULT_MESSAGE))
                            .error(new Error())
                            .build());
        }else{
            return ResponseEntity.badRequest()
                    .body(SMSResponse.builder()
                            .code(400)
                            .message(null)
                            .data(new Data(Data.DEFAULT_MESSAGE))
                            .error(new Error("코드가 올바르지 않습니다.",1000))
                            .build());
        }

    }




}
