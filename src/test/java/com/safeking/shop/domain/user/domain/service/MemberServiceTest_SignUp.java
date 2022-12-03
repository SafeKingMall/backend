package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.CheckSignUp;
import com.safeking.shop.domain.user.domain.service.dto.CriticalItemsDto;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class MemberServiceTest_SignUp {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemoryMemberRepository memoryMemberRepository;
    @Autowired
    MemberService memberService;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    Long userId;

    @DisplayName("social_sign_up")
    @ParameterizedTest(name = "{index} {displayName}")
    @CsvSource({
            "kakao, id, 12345, email, kms1999@naver.com"//카카오 시
            ,"google, sub, 12345, email, kms1111@google.com"//구글 시
    })
    void socialSignUp(@AggregateWith(SocialAggregator2.class) String registrationId, @AggregateWith(SocialAggregator.class) Map<String, Object> data) {
        //given
        CheckSignUp checkSignUp = memberService.socialLogin(registrationId, data);
        //when
        Member member = memoryMemberRepository.findById(checkSignUp.getId()).orElseThrow();
        //then
        assertThat(member.getUsername()).isEqualTo(registrationId+"_"+"12345");
        assertThat(member.getEmail()).isEqualTo(data.get("email"));

    }
    static class SocialAggregator implements ArgumentsAggregator{
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            Map<String, Object> data=new HashMap<>();
            data.put(accessor.getString(1),accessor.get(2));
            data.put(accessor.getString(3),accessor.get(4));

            return data;
        }
    }
    static class SocialAggregator2 implements ArgumentsAggregator{
        @Override
        public Object aggregateArguments(ArgumentsAccessor accessor, ParameterContext context) throws ArgumentsAggregationException {
            return accessor.getString(0);
        }
    }

    @Test
    @DisplayName("1. addCriticalItems")
    @Order(1)
    void addCriticalItems() {
        //given
        CriticalItemsDto criticalItemsDto = CriticalItemsDto.builder()
                .username("user")
                .password("password")
                .email("email")
                .build();
        //when
        userId = memberService.addCriticalItems(criticalItemsDto);

        //then
        Member member = memoryMemberRepository.findById(userId).orElseThrow();

        assertThat(member)
                .extracting("username","email")
                .containsExactly("user","email");
    }


    @Test
    void addAuthenticationInfo() {
    }

    @Test
    void addMemberInfo() {
    }

    @Test
    void changeMemoryToDB() {
    }
}