package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.service.dto.AuthenticationInfoDto;
import com.safeking.shop.domain.user.domain.service.dto.CriticalItemsDto;
import com.safeking.shop.domain.user.domain.service.dto.MemberInfoDto;
import com.safeking.shop.domain.user.web.request.signuprequest.CriticalItems;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class DormantMemberServiceTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    DormantMemberService dormantMemberService;
    @Autowired
    MemoryDormantRepository dormantRepository;
    @Autowired
    CustomBCryPasswordEncoder encoder;

    Long dormantId;
    Address address = new Address("서울시", "마포대로", "111");

    @BeforeAll
    void before(){
        //given
        GeneralMember dormant = GeneralMember.builder()
                .username("dormant")
                .password("1234")
                .roles("ROLE_USER")
                .status(MemberStatus.HUMAN)
                .accountNonLocked(false)
                .build();
        memberRepository.save(dormant);
    }

    @Test
    @Order(1)
    @DisplayName("1. addCriticalItems")
    void addCriticalItems() {
        //given
        CriticalItemsDto criticalItemsDto = CriticalItemsDto.builder()
                .username("dormant")
                .password("changePassword")
                .email("email")
                .build();
        //when
        dormantId = dormantMemberService.addCriticalItems(criticalItemsDto);

        //then
        Member member = dormantRepository.findById(dormantId).orElseThrow();

        assertThat(member)
                .extracting("username","email")
                .containsExactly("dormant","email");
    }

    @Test
    @Order(2)
    @DisplayName("2. addAuthenticationInfo")
    void addAuthenticationInfo() {
        //given
        AuthenticationInfoDto authenticationInfoDto = AuthenticationInfoDto.builder()
                .name("dormant")
                .birth("birth")
                .phoneNumber("01012345678")
                .build();
        //when
        dormantMemberService.addAuthenticationInfo(dormantId,authenticationInfoDto);

        //then
        Member findMember = dormantRepository.findById(dormantId).orElseThrow();

        assertThat(findMember)
                .extracting("name","birth","phoneNumber")
                .containsExactly("dormant","birth","01012345678");
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
        dormantMemberService.addMemberInfo(dormantId,memberInfoDto);

        //then
        Member findMember = dormantRepository.findById(dormantId).orElseThrow();

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
    @DisplayName("4. revertCommonAccounts")
    @Order(4)
    void revertCommonAccounts() {
        //given
        Boolean agreement= true;
        //when
        dormantMemberService.revertCommonAccounts(dormantId,agreement);
        //then
        Member findMember = memberRepository.findByUsername("dormant").orElseThrow();

        assertThat(findMember).extracting
                ("name"
                        ,"birth"
                        ,"username"
                        ,"password"
                        ,"email"
                        ,"roles"
                        ,"phoneNumber"
                        ,"companyName"
                        ,"companyRegistrationNumber"
                        ,"corporateRegistrationNumber"
                        ,"representativeName"
                        ,"contact"
                        ,"agreement"
                        ,"accountNonLocked"
                        ,"status"
                )
                .containsExactly
                        ("dormant"
                        ,"birth"
                        ,"dormant"
                        ,"changePassword"
                        ,"email"
                        , "ROLE_USER"
                        ,"01012345678"
                        ,"safeking"
                        ,"111"
                        ,"222"
                        ,"MS"
                        ,"contact"
                        ,true
                        ,true
                        ,MemberStatus.COMMON
                        );
    }
}