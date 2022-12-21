package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.DormantMemberService;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.web.request.signuprequest.AgreementInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.AuthenticationInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.CriticalItems;
import com.safeking.shop.domain.user.web.request.signuprequest.MemberInfo;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.safeking.shop.global.DocumentFormatGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MemberControllerTest_dormant extends MvcTest {
    @Autowired
    MemoryDormantRepository memoryMemberRepository;
    @Autowired
    DormantMemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TestUserHelper userHelper;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @BeforeEach
    void before(){
        memoryMemberRepository.clearStore();
    }
    @AfterEach
    void after(){
        memoryMemberRepository.clearStore();
    }
    Long memberId=null;


    @BeforeEach
    public void init(){
        GeneralMember member = GeneralMember.builder()
                .username("kms199711")
                .roles("ROLE_USER")
                .accountNonLocked(false)
                .status(MemberStatus.HUMAN)
                .build();
        GeneralMember generalMember = memberRepository.save(member);
        memberId=generalMember.getId();
    }
    @Test
    @DisplayName("1. dormantCriticalItems")
    void dormantCriticalItems() throws Exception {
        //given
        CriticalItems criticalItems = getCriticalItems();
        String content = om.writeValueAsString(criticalItems);
        /**
         * post 요청시에 contentType 과 accept 를 설정
         **/
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/criticalItems")

                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(memberId.toString());
        //docs
        resultActions.andDo(
                        document("dormantCriticalItems"
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("기존 회원의 아이디")
                                ,fieldWithPath("password").attributes(PWValidation()).description("password")
                                ,fieldWithPath("email").type(JsonFieldType.STRING).attributes(EmailValidation()).description("email")
                                )
                        )
                );
    }
    @Test
    @DisplayName("2. dormantAuthenticationInfo")
    void dormantAuthenticationInfo() throws Exception {
        //given
        CriticalItems criticalItems = getCriticalItems();
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());

        AuthenticationInfo authenticationInfo = getAuthenticationInfo();
        String requestData = om.writeValueAsString(authenticationInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/authenticationInfo/{memberId}",memberId)
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(Long.toString(memberId));
        //docs
        /**
         * pathParameters 가 있다면  RestDocumentationRequestBuilders.post 를 사용
         **/
        resultActions.andDo(
                document("dormantAuthenticationInfo"
                ,pathParameters(
                        parameterWithName("memberId").description("임시 회원 아이디")
                        )
                ,requestFields(
                        fieldWithPath("name").attributes(InputValidation()).description("name")
                        ,fieldWithPath("birth").attributes(BirthValidation()).description("birth")
                        ,fieldWithPath("phoneNumber").attributes(PhoneNumberValidation()).description("phoneNumber")
                        )
                )
        );
    }

    @Test
    @DisplayName("3. dormantMemberInfo")
    void dormantMemberInfo() throws Exception {
         //given
        CriticalItems criticalItems = getCriticalItems();
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());

        MemberInfo memberInfo = getMemberInfo();
        String requestData = om.writeValueAsString(memberInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/memberInfo/{memberId}", memberId)
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(Long.toString(memberId));
        //docs
        resultActions.andDo(
                document("dormantMemberInfo"
                        ,pathParameters(
                                parameterWithName("memberId").description("임시 회원 아이디")
                        )
                        ,requestFields(
                                fieldWithPath("companyName").attributes(InputValidation()).description("companyName")
                                ,fieldWithPath("companyRegistrationNumber").attributes(companyRegistrationNumberValidation()).description("사업자 등록 번호")
                                ,fieldWithPath("corporateRegistrationNumber").optional().attributes(InputValidation()).description("법인 번호")
                                ,fieldWithPath("representativeName").attributes(InputValidation()).description("대표자 명")
                                ,fieldWithPath("basicAddress").attributes(InputValidation()).description("기본 주소")
                                ,fieldWithPath("detailedAddress").attributes(InputValidation()).description("상세 주소")
                                ,fieldWithPath("zipcode").attributes(InputValidation()).description("우편 번호")
                                ,fieldWithPath("contact").optional().attributes(InputValidation()).description("연락처")
                        )
                )
        );
    }
    @Test
    @DisplayName("4. dormantAgreementInfo")
    void dormantAgreementInfo() throws Exception {
        //given
        CriticalItems criticalItems = getCriticalItems();
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());

        AuthenticationInfo authenticationInfo = getAuthenticationInfo();
        memberService.addAuthenticationInfo(memberId,authenticationInfo.toServiceDto());

        MemberInfo memberInfo = getMemberInfo();
        memberService.addMemberInfo(memberId,memberInfo.toServiceDto());

        AgreementInfo agreementInfo = getAgreementInfo();
        String requestData = om.writeValueAsString(agreementInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/agreementInfo/{memberId}",memberId)
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        //docs
        resultActions.andDo(
                document("dormantAgreementInfo"
                        ,pathParameters(
                                parameterWithName("memberId").description("임시 회원 아이디")
                        )
                        ,requestFields(
                                fieldWithPath("userAgreement").attributes(InputValidation()).description("userAgreement")
                                ,fieldWithPath("infoAgreement").attributes(BooleanValidation()).description("infoAgreement")
                        )
                )
        );
    }

    private static AgreementInfo getAgreementInfo() {
        AgreementInfo agreementInfo = AgreementInfo.builder()
                .userAgreement(true)
                .infoAgreement(true)
                .build();
        return agreementInfo;
    }

    private OauthMember createDormant() {
        OauthMember oauthMember = OauthMember.builder()
                .username("kakao_123456789")
                .password(encoder.encode("safeking"))
                .accountNonLocked(false)
                .status(MemberStatus.HUMAN)
                .build();
        memberRepository.save(oauthMember);
        return oauthMember;
    }
    private static MemberInfo getMemberInfo() {
        MemberInfo memberInfo = MemberInfo.builder()
                .companyName("safeking")
                .companyRegistrationNumber("111-22-12345")
                .corporateRegistrationNumber("111111-1234567")
                .representativeName("ms")
                .basicAddress("서울시")
                .detailedAddress("마포대로")
                .zipcode("111")
                .contact("01012345678")
                .build();
        return memberInfo;
    }

    private static AuthenticationInfo getAuthenticationInfo() {
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .name("name")
                .birth("20001202")
                .phoneNumber("01082460887")
                .build();
        return authenticationInfo;
    }

    private static CriticalItems getCriticalItems() {
        CriticalItems criticalItems = CriticalItems.builder()
                .username("kms199711")
                .password("kms199711*")
                .email("kms1997@naver.com")
                .build();
        return criticalItems;
    }


}