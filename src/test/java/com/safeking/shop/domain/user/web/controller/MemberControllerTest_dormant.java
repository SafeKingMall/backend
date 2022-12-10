package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.web.request.signuprequest.AgreementInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.AuthenticationInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.CriticalItems;
import com.safeking.shop.domain.user.web.request.signuprequest.MemberInfo;
import com.safeking.shop.global.MvcTest;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
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
    MemoryMemberRepository memoryMemberRepository;
    @Autowired
    MemberService memberService;
    @BeforeEach
    void before(){
        memoryMemberRepository.clearStore();
    }
    @AfterEach
    void after(){
        memoryMemberRepository.clearStore();
    }


    @Test
    @DisplayName("1. signUpCriticalItems")
    void signUpCriticalItems() throws Exception {
        //given
        CriticalItems criticalItems = CriticalItems.builder()
                .username("kms199711")
                .password("kms199711*")
                .email("kms1997@naver.com")
                .build();

        String content = om.writeValueAsString(criticalItems);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/signup/criticalItems")

                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo("1");
        //docs
        resultActions.andDo(
                        document("signUpCriticalItems"
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("username")
                                ,fieldWithPath("password").attributes(PWValidation()).description("password")
                                ,fieldWithPath("email").type(JsonFieldType.STRING).attributes(EmailValidation()).description("email")
                                )
                        )
                );
    }
    @Test
    @DisplayName("1. socialSignUp")
    void socialSignUp() throws Exception {
        //given
        String registrationId="kakao";

        Map<String, Object> data =new HashMap<>();
        data.put("id","123456789");
        data.put("email","kms199719@naver.com");

        String content = om.writeValueAsString(data);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/oauth/{registrationId}",registrationId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo("1");
        //docs
        resultActions.andDo(
                document("socialSignUp"
                        ,requestFields(
                                fieldWithPath("id").attributes(IdValidation()).description("id")
                                ,fieldWithPath("email").attributes(EmailValidation()).description("email")
                        )
                )
        );
    }
    @Test
    @DisplayName("2. signUpAuthenticationInfo")
    void signUpAuthenticationInfo() throws Exception {
        //given
        CriticalItems criticalItems = CriticalItems.builder()
                .username("kms199711")
                .password("kms199711*")
                .email("kms1997@naver.com")
                .build();
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());

        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .name("name")
                .birth("20001202")
                .phoneNumber("01082460887")
                .build();
        String requestData = om.writeValueAsString(authenticationInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/signup/authenticationInfo/{memberId}",memberId)
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(Long.toString(memberId));
        //docs
        resultActions.andDo(
                document("signUpAuthenticationInfo"
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
    @DisplayName("3. signUpMemberInfo")
    void signUpMemberInfo() throws Exception {
         //given
        CriticalItems criticalItems = CriticalItems.builder()
                .username("kms199711")
                .password("kms199711*")
                .email("kms1997@naver.com")
                .build();
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());

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
        String requestData = om.writeValueAsString(memberInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/signup/memberInfo/{memberId}", memberId)
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(Long.toString(memberId));
        //docs
        resultActions.andDo(
                document("signUpMemberInfo"
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
    @DisplayName("4. signUpAgreementInfo")
    void signUpAgreementInfo() throws Exception {
        //given
        CriticalItems criticalItems = CriticalItems.builder()
                .username("kms199711")
                .password("kms199711*")
                .email("kms1997@naver.com")
                .build();
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());

        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .name("name")
                .birth("971202")
                .phoneNumber("01082460887")
                .build();
        memberService.addAuthenticationInfo(memberId,authenticationInfo.toServiceDto());

        MemberInfo memberInfo = MemberInfo.builder()
                .companyName("safeking")
                .companyRegistrationNumber("111")
                .corporateRegistrationNumber("222")
                .representativeName("ms")
                .basicAddress("서울시")
                .detailedAddress("마포대로")
                .zipcode("111")
                .contact("01012345678")
                .build();
        memberService.addMemberInfo(memberId,memberInfo.toServiceDto());

        AgreementInfo agreementInfo = AgreementInfo.builder()
                .userAgreement(true)
                .infoAgreement(true)
                .build();
        String requestData = om.writeValueAsString(agreementInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/signup/agreementInfo/{memberId}",memberId)
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        //docs
        resultActions.andDo(
                document("signUpAgreementInfo"
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
    @Test
    @DisplayName("memoryMemberRepoClear")
    void memoryMemberRepoClear() throws Exception {
        //given
        CriticalItems criticalItems = CriticalItems.builder()
                .username("kms199711")
                .password("kms199711*")
                .email("kms1997@naver.com")
                .build();
        assertNotNull(criticalItems);
        Long memberId = memberService.addCriticalItems(criticalItems.toServiceDto());
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/signup/memoryClear/{memberId}", 1L))
                .andExpect(status().isOk());
        //then
        assertThrows(NoSuchElementException.class,()->memoryMemberRepository.findById(memberId).orElseThrow());
        //docs
        resultActions.andDo(
                document("memoryMemberRepoClear"
                        ,pathParameters(
                                parameterWithName("memberId").description("임시 회원 아이디")
                        )
                )
        );
    }
}