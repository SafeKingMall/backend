package com.safeking.shop.domain.user.web.query.repository;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MemberQueryRepositoryTest {
    @Autowired
    MemberQueryRepository memberQueryRepository;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @BeforeEach
    void init(){
        for (int i = 1; i <=30 ; i++) {
            Member user = GeneralMember.builder()
                    .name("user"+i)
                    .birth("971202")
                    .username("testUser"+i)
                    .password(encoder.encode("testUser"+i+"*"))
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
            user.addLastLoginTime();
            memberRepository.save(user);
        }
    }
    @DisplayName("searchAllCondition")
    @ParameterizedTest(name = "{index} {displayName}")
    @ValueSource(strings = {"user1","user2"})
    @NullAndEmptySource
    void searchAllCondition(String value) {
        PageRequest pageRequest = PageRequest.of(0, 10);
        memberQueryRepository.searchAllCondition(value,pageRequest).stream()
                .forEach(
                        memberListDto -> System.out.println("memberListDto.getName() = " + memberListDto.getName())
                );
    }
}