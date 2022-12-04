package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
class CacheMemberRepositoryTest {

    @Autowired
    MemberRedisRepository cacheMemberRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;


    @Test
    void findByUsername() {
        //given
        Member user = GeneralMember.builder()
                .id(1L)
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

        cacheMemberRepository.save(new RedisMember(user.getRoles(),user.getUsername()));
        //when
        RedisMember findMember = cacheMemberRepository.findByUsername(user.getUsername()).orElseThrow();
        //then
        Assertions.assertThat(findMember.getRoles()).isEqualTo(user.getRoles());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(user.getUsername());
    }
}