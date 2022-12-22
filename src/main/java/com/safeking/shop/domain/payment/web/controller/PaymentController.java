package com.safeking.shop.domain.payment.web.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/admin")
public class PaymentController {
    @PostMapping("/payment/receive-callback")
    public String receiveCallback() {
        return "";
    }
}
