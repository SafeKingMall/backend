package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.global.jwt.JwtPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @GetMapping("/temp")
    public String test1(@AuthenticationPrincipal JwtPrincipal jwtPrincipal) {
        log.info("{}", jwtPrincipal.toString());
        return "temp";
    }

    @GetMapping("/active")
    public String test2(@AuthenticationPrincipal JwtPrincipal jwtPrincipal) {
        log.info("{}", jwtPrincipal.toString());
        return "active";
    }

    @GetMapping("/admin")
    public String test3(@AuthenticationPrincipal JwtPrincipal jwtPrincipal) {
        log.info("{}", jwtPrincipal.toString());
        return "admin";
    }
}
