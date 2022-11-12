package com.safeking.shop.domain.user.web.controller;

import java.util.Date;
import java.util.Map;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.oauth.provider.FaceBookUserInfo;
import com.safeking.shop.global.oauth.provider.GoogleUserInfo;
import com.safeking.shop.global.oauth.provider.NaverUserInfo;
import com.safeking.shop.global.oauth.provider.Oauth2UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SocialLoginController {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;

    @PostMapping("/oauth/jwt/{registrationId}")
    public String jwtCreate(@PathVariable String registrationId, @RequestBody Map<String, Object> data) {

        Oauth2UserInfo oauth2UserInfo = null;

        if (registrationId.equals("google")) {
            log.info("google login request");

            oauth2UserInfo = new GoogleUserInfo(data);
        } else {
            //추가 고민 중
        }

        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String name = oauth2UserInfo.getName();
        String username = provider + "_" + providerId;
        String password = encoder.encode("safeking");
        String email = oauth2UserInfo.getEmail();//구글이 준 email
        String role = "ROLE_USER";

        Member oauthMember = memberRepository.findByUsername(username).orElse(null);
        if (oauthMember == null) {
            oauthMember = OauthMember.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .roles(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(oauthMember);
        } else {
            log.info("Oauth 를 톤해 회원가입을 한 적이 있다.");
        }

        //jwt 발행
        String jwtToken = JWT.create()
                .withSubject(oauthMember.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 100)))
                .withClaim("id", oauthMember.getId())
                .withClaim("username", oauthMember.getUsername())
                .sign(Algorithm.HMAC512("safeKing"));

        return jwtToken;
    }

}
