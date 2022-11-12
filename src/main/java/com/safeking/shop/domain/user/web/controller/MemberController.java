package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.web.query.service.MemberQueryService;
import com.safeking.shop.domain.user.web.request.SignUpRequest;
import com.safeking.shop.domain.user.web.response.signup.Data;
import com.safeking.shop.domain.user.web.response.signup.SignUpResponse;
import com.safeking.shop.global.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest){

        GeneralSingUpDto generalSingUpDto = signUpRequest.toServiceDto();
        memberService.join(generalSingUpDto);

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .code(200)
                .message(SignUpResponse.SUCCESS_MESSAGE)
                .data(new Data(Data.message))
                .error(new Error())
                .build();

        return ResponseEntity.ok().body(signUpResponse);
    }




}
