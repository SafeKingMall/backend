package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.AuthenticationInfoDto;
import com.safeking.shop.domain.user.domain.service.dto.CheckSignUp;
import com.safeking.shop.domain.user.domain.service.dto.CriticalItemsDto;
import com.safeking.shop.domain.user.domain.service.dto.MemberInfoDto;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
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
    Address address = new Address("서울시", "마포대로", "111");

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
    @Order(2)
    @DisplayName("2. addAuthenticationInfo")
    void addAuthenticationInfo() {
        //given
        AuthenticationInfoDto authenticationInfoDto = AuthenticationInfoDto.builder()
                .name("user")
                .birth("birth")
                .phoneNumber("01012345678")
                .build();
        //when
        memberService.addAuthenticationInfo(userId,authenticationInfoDto);

        //then
        Member findMember = memoryMemberRepository.findById(userId).orElseThrow();

        assertThat(findMember)
                .extracting("name","birth","phoneNumber")
                .containsExactly("user","birth","01012345678");
    }

    @Test
    @Order(3)
    @DisplayName("3. addAuthenticationInfo")
    void addMemberInfo() {
        //given
        MemberInfoDto memberInfoDto = MemberInfoDto.builder()
                .companyName("safeking")
                .companyRegistrationNumber("111")
                .corporateRegistrationNumber("222")
                .representativeName("MS")
                .address(address)
                .contact("contact")
                .build();
        //when
        memberService.addMemberInfo(userId,memberInfoDto);

        //then
        Member findMember = memoryMemberRepository.findById(userId).orElseThrow();

        assertThat(findMember)
                .extracting( "companyName"
                        ,"companyRegistrationNumber"
                        ,"corporateRegistrationNumber"
                        ,"representativeName"
                        ,"address"
                        ,"contact"
                )
                .containsExactly(
                        "safeking"
                        ,"111"
                        ,"222"
                        ,"MS"
                        ,address
                        ,"contact"
                );
    }


    @Test
    @Order(4)
    @DisplayName("4. changeMemoryToDB")
    void changeMemoryToDB() {
        //given
        Boolean agreements=true;
        //when
        memberService.changeMemoryToDB(userId,agreements);
        //then
        Member user = memberRepository.findByUsername("user").orElseThrow();
        assertAll(
                ()->assertThat(user.getName()).isEqualTo("user")
                ,()->assertThat(user.getBirth()).isEqualTo("birth")
                ,()->assertThat(user.getUsername()).isEqualTo("user")
                ,()->assertThat(user.getEmail()).isEqualTo("email")
                ,()->assertThat(user.getRoles()).isEqualTo("ROLE_USER")
                ,()->assertThat(user.getPhoneNumber()).isEqualTo("01012345678")
                ,()->assertThat(user.getCompanyName()).isEqualTo("safeking")
                ,()->assertThat(user.getCompanyRegistrationNumber()).isEqualTo("111")
                ,()->assertThat(user.getCorporateRegistrationNumber()).isEqualTo("222")
                ,()->assertThat(user.getRepresentativeName()).isEqualTo("MS")
                ,()->assertThat(user.getContact()).isEqualTo("contact")
                ,()->assertThat(user.getAgreement()).isEqualTo(true)
                ,()->assertThat(user.getAccountNonLocked()).isEqualTo(true)
                ,()->assertThat(user.getStatus()).isEqualTo(MemberStatus.COMMON)
        );
    }
}