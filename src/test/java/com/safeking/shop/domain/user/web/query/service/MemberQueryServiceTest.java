package com.safeking.shop.domain.user.web.query.service;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.web.query.repository.MemberQueryRepository;
import com.safeking.shop.domain.user.web.response.MemberDetails;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberQueryServiceTest {
    @Autowired
    MemberQueryService memberQueryService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @BeforeEach
    void init(){
            Member user = GeneralMember.builder()
                    .name("user")
                    .birth("971202")
                    .username("testUser")
                    .password(encoder.encode("testUser*"))
                    .email("kms199719@naver.com")
                    .roles("ROLE_USER")
                    .phoneNumber("01082460887")
                    .companyName("safeking")
                    .companyRegistrationNumber("111")
                    .corporateRegistrationNumber("222")
                    .representativeName("MS")
                    .address(new Address("서울시","마포대로","111"))
                    .agreement(true)
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .build();
            memberRepository.save(user);
    }
    @Test
    void showMemberDetails() {
        //given
        String username="testUser";
        //when
        MemberDetails testUser = memberQueryService.showMemberDetails(username);
        //then
        assertAll(
                ()->assertThat(testUser.getName()).isEqualTo("user")
                ,()->assertThat(testUser.getBirth()).isEqualTo("971202")
        );
    }
}