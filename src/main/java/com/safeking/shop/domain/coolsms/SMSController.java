package com.safeking.shop.domain.coolsms;

import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SMSController {

    private final SMSService smsService;

    @GetMapping("api/v1/coolsms")
    public String sendCodeToClient(@RequestParam String clientPhoneNumber) throws CoolsmsException {
        return smsService.sendCodeToClient(clientPhoneNumber);
    }

}
