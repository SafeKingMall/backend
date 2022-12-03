package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
import com.safeking.shop.domain.user.web.request.UpdateRequest;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @BeforeEach
    void init(){
        GeneralMember member = GeneralMember.builder()
                .name("user")
                .birth("birth")
                .username("username")
                .password(encoder.encode("password"))
                .email("email")
                .roles("ROLE_USER")
                .phoneNumber("01012345678")
                .companyName("safeking")
                .companyRegistrationNumber("111")
                .corporateRegistrationNumber("222")
                .representativeName("MS")
                .contact("contact")
                .address(new Address("서울시", "마포대로", "111"))
                .agreement(true)
                .accountNonLocked(true)
                .status(MemberStatus.COMMON)
                .build();
        member.addLastLoginTime();
        memberRepository.save(member);
    }

    @Test
    @DisplayName("1. idDuplicationCheck")
    void idDuplicationCheck() {
        //given
        String username1="username";
        String username2="username2";
        //when
        boolean NO = memberService.idDuplicationCheck(username1);
        boolean YES = memberService.idDuplicationCheck(username2);
        //then
        assertAll(
                ()->assertThat(NO).isEqualTo(false),
                ()->assertThat(YES).isEqualTo(true)
        );

    }

    @Test
    @DisplayName("2. updateMemberInfo")
    void updateMemberInfo() {
        //given
        MemberUpdateDto memberUpdateDto = MemberUpdateDto.builder()
                .name("nameChange")
                .birth("birthChange")
                .representativeName("representativeNameChange")
                .phoneNumber("phoneNumberChange")
                .companyRegistrationNumber("companyRegistrationNumberChange")
                .corporateRegistrationNumber("corporateRegistrationNumberChange")
                .address(new Address("basicAddressChange", "detailedAddressChange", "zipcodeChange"))
                .build();
        //when
        memberService.updateMemberInfo("username",memberUpdateDto);
        //then
        Member member = memberRepository.findByUsername("username").orElseThrow();

        assertAll(
                ()->assertThat(member.getName()).isEqualTo("nameChange")
                ,()->assertThat(member.getBirth()).isEqualTo("birthChange")
                ,()->assertThat(member.getRepresentativeName()).isEqualTo("representativeNameChange")
                ,()->assertThat(member.getPhoneNumber()).isEqualTo("phoneNumberChange")
                ,()->assertThat(member.getCompanyRegistrationNumber()).isEqualTo("companyRegistrationNumberChange")
                ,()->assertThat(member.getCorporateRegistrationNumber()).isEqualTo("corporateRegistrationNumberChange")
                ,()->assertThat(member.getAddress())
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("basicAddressChange", "detailedAddressChange", "zipcodeChange"))
       );
    }

    @Test
    @DisplayName("3. humanAccountConverterBatch")
    void humanAccountConverterBatch() throws InterruptedException {
        //given
        for (int i = 1; i <=5 ; i++) {
            GeneralMember member = GeneralMember.builder()
                    .name("user"+i)
                    .birth("birth")
                    .username("username"+i)
                    .password(encoder.encode("password"))
                    .email("email")
                    .roles("ROLE_USER")
                    .phoneNumber("01012345678")
                    .companyName("safeking")
                    .companyRegistrationNumber("111")
                    .corporateRegistrationNumber("222")
                    .representativeName("MS")
                    .contact("contact")
                    .address(new Address("서울시", "마포대로", "111"))
                    .agreement(true)
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .build();
            member.addLastLoginTime();
            memberRepository.save(member);
        }
        Thread.sleep(1000*Member.MEMBER_HUMAN_TIME);

        //when
        memberService.humanAccountConverterBatch();

        //then
        memberRepository.findAll().stream().forEach(member -> {
             assertThat(member.getAccountNonLocked()).isFalse();
             assertThat(member.getStatus()).isEqualTo(MemberStatus.HUMAN);
        });
    }

    @Test
    void sendTemporaryPassword() {
    }
}