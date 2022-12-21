package com.safeking.shop.domain.user.domain.entity.member;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@ActiveProfiles("test")
class MemberTest {
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Test
    void getRoleList() {
        GeneralMember member = GeneralMember.builder()
                .roles("ROLE_USER,ROLE_MANAGER")
                .build();

        List<String> roleList = member.getRoleList();

        assertThat(roleList).containsExactly("ROLE_USER","ROLE_MANAGER");
    }
    @Test
    void isCheckedItem(){
        //given
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
                .build();
        //when
        boolean checkedItem = user.isCheckedItem();
        //then
        assertThat(checkedItem).isEqualTo(true);
    }
//    @Test
//    void convertHumanAccount() throws InterruptedException {
//        //given
//        Member user = GeneralMember.builder()
//                .name("user")
//                .birth("971202")
//                .username("testUser")
//                .password(encoder.encode("testUser*"))
//                .email("kms199719@naver.com")
//                .roles("ROLE_USER")
//                .phoneNumber("01082460887")
//                .companyName("safeking")
//                .companyRegistrationNumber("111")
//                .corporateRegistrationNumber("222")
//                .representativeName("MS")
//                .address(new Address("서울시","마포대로","111"))
//                .agreement(true)
//                .accountNonLocked(true)
//                .status(MemberStatus.COMMON)
//                .build();
//        user.addLastLoginTime();
//
//        //when
//        Thread.sleep(1000*Member.MEMBER_HUMAN_TIME);
//        user.convertHumanAccount();
//        //then
//        assertAll(
//                ()->assertThat(user.getStatus()).isEqualTo(MemberStatus.HUMAN),
//                ()->assertThat(user.getAccountNonLocked()).isEqualTo(false),
//
//                ()->assertThat(user.getName()).isEqualTo(null),
//                ()->assertThat(user.getBirth()).isEqualTo(null),
//                ()->assertThat(user.getEmail()).isEqualTo(null),
//                ()->assertThat(user.getPhoneNumber()).isEqualTo(null),
//                ()->assertThat(user.getCompanyName()).isEqualTo(null),
//                ()->assertThat(user.getRepresentativeName()).isEqualTo(null),
//                ()->assertThat(user.getCompanyRegistrationNumber()).isEqualTo(null),
//                ()->assertThat(user.getCorporateRegistrationNumber()).isEqualTo(null),
//                ()->assertThat(user.getAddress()).isEqualTo(null),
//                ()->assertThat(user.getContact()).isEqualTo(null),
//                ()->assertThat(user.getAgreement()).isEqualTo(null)
//        );
//    }
}