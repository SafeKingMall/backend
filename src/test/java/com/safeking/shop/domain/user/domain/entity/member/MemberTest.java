package com.safeking.shop.domain.user.domain.entity.member;

import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class MemberTest {
    private final CustomBCryPasswordEncoder encoder;

    @Test
    void getRoleList() {

        //member 를 생성
        Member user = GeneralMember.builder()
                .username("user")
                .password(encoder.encode("1234"))
                .roles("ROLE_USER,ROLE_MANAGER").build();


    }
}