package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.web.request.signup.SignUpRequest;
import com.safeking.shop.domain.user.web.response.signup.Data;
import com.safeking.shop.domain.user.web.response.signup.SignUpResponse;
import com.safeking.shop.global.Error;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

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
    /**
     * 소셜로그인 구현 중*/

//    @PostMapping("/oauth/jwt/{registrationId}")
//    public String jwtCreate(@PathVariable String registrationId, @RequestBody Map<String, Object> data) {
//
//        Oauth2UserInfo oauth2UserInfo = null;
//
//        if (registrationId.equals("google")) {
//            log.info("google login request");
//
//            oauth2UserInfo = new GoogleUserInfo(data);
//        } else {
//            //추가 고민 중
//        }
//
//        String provider = oauth2UserInfo.getProvider();
//        String providerId = oauth2UserInfo.getProviderId();
//        String name = oauth2UserInfo.getName();
//        String username = provider + "_" + providerId;
//        String password = encoder.encode("safeking");
//        String email = oauth2UserInfo.getEmail();//구글이 준 email
//        String role = "ROLE_USER";
//
//        Member oauthMember = memberRepository.findByUsername(username).orElse(null);
//        if (oauthMember == null) {
//            oauthMember = OauthMember.builder()
//                    .username(username)
//                    .password(password)
//                    .name(name)
//                    .email(email)
//                    .roles(role)
//                    .provider(provider)
//                    .providerId(providerId)
//                    .build();
//            memberRepository.save(oauthMember);
//        } else {
//            log.info("Oauth 를 톤해 회원가입을 한 적이 있다.");
//        }
//
//        //jwt 발행
//        String jwtToken = JWT.create()
//                .withSubject(oauthMember.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 100)))
//                .withClaim("id", oauthMember.getId())
//                .withClaim("username", oauthMember.getUsername())
//                .sign(Algorithm.HMAC512("safeKing"));
//
//        return jwtToken;
//    }




}
