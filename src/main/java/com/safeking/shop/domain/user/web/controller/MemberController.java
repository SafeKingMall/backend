package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.web.query.service.MemberQueryService;
import com.safeking.shop.domain.user.web.request.SignUpRequest;
import com.safeking.shop.domain.user.web.response.SignUpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;

    @PostMapping("/signUp")
    public SignUpResponse signUp(@RequestBody SignUpRequest signUpRequest){

        GeneralSingUpDto generalSingUpDto = signUpRequest.toServiceDto();

        memberService.join(generalSingUpDto);

        return new SignUpResponse();
    }

    @GetMapping("/success")
    public ResponseEntity<?> signInSuccess(HttpServletResponse response) {
        //임시로 log 를 찍음
        log.info("JWT 토큰= {}",response.getHeader("Authorization"));

        return ResponseEntity.ok("login success");
    }


}
