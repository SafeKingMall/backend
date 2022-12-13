package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.service.DormantMemberService;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.dto.CheckSignUp;
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
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.safeking.shop.global.DocumentFormatGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MemberControllerTest_social_dormant extends MvcTest {
    @Autowired
    MemoryDormantRepository memoryMemberRepository;
    @Autowired
    DormantMemberService dormantMemberService;
    @Autowired
    MemberService memberService;
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
        OauthMember oauthMember = getOauthMember();
        OauthMember member = memberRepository.save(oauthMember);
        memberId=member.getId();
    }
    @Test
    @DisplayName("1. dormantCriticalItems_social")
    void dormantCriticalItems_social() throws Exception {
        //given
        String registrationId="kakao";
        Map<String, Object> data = createData();

        String content = om.writeValueAsString(data);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/oauth/{registrationId}",registrationId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        //then
        assertThat(resultActions.andReturn().getResponse().getStatus()).isEqualTo(403);
        //docs
        resultActions.andDo(
                document("dormantCriticalItems_social"
                        ,pathParameters(
                                parameterWithName("registrationId").description("kakao or google")
                        )
                        ,requestFields(
                                fieldWithPath("id").attributes(IdValidation()).description("구글일시에는 sub, 카카오 일시에는 id")
                                ,fieldWithPath("email").type(JsonFieldType.STRING).attributes(EmailValidation()).description("email")
                        )
                )
        );
    }

    @Test
    @DisplayName("2. dormantAuthenticationInfo_social")
    void dormantAuthenticationInfo_social() throws Exception {
        //given
        String registrationId="kakao";
        Map<String, Object> data = createData();

        CheckSignUp checkSignUp = memberService.socialLogin(registrationId, data);
        AuthenticationInfo authenticationInfo = getAuthenticationInfo();

        String requestData = om.writeValueAsString(authenticationInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/authenticationInfo/{memberId}",checkSignUp.getId())
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(Long.toString(checkSignUp.getId()));
        //docs
        resultActions.andDo(
                document("dormantAuthenticationInfo_social"
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
    @DisplayName("3. dormantMemberInfo_social")
    void dormantMemberInfo_social() throws Exception {
         //given
        String registrationId="kakao";
        Map<String, Object> data = createData();

        CheckSignUp checkSignUp = memberService.socialLogin(registrationId, data);

        MemberInfo memberInfo = getMemberInfo();
        String requestData = om.writeValueAsString(memberInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/memberInfo/{memberId}", checkSignUp.getId())
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(Long.toString(memberId));
        //docs
        resultActions.andDo(
                document("dormantMemberInfo_social"
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
    @DisplayName("4. dormantAgreementInfo_social")
    void dormantAgreementInfo_social() throws Exception {
        //given
        String registrationId="kakao";
        Map<String, Object> data = createData();

        CheckSignUp checkSignUp = memberService.socialLogin(registrationId, data);

        AuthenticationInfo authenticationInfo = getAuthenticationInfo();
        dormantMemberService.addAuthenticationInfo(checkSignUp.getId(),authenticationInfo.toServiceDto());

        MemberInfo memberInfo = getMemberInfo();
        dormantMemberService.addMemberInfo(checkSignUp.getId(),memberInfo.toServiceDto());

        AgreementInfo agreementInfo = AgreementInfo.builder()
                .userAgreement(true)
                .infoAgreement(true)
                .build();
        String requestData = om.writeValueAsString(agreementInfo);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/dormant/agreementInfo/{memberId}",checkSignUp.getId())
                        .content(requestData)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //docs
        resultActions.andDo(
                document("dormantAgreementInfo_social"
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

    private OauthMember getOauthMember() {
        OauthMember oauthMember = OauthMember.builder()
                .username("kakao_12356789")
                .password(encoder.encode("safeking"))
                .accountNonLocked(false)
                .roles("ROLE_USER")
                .status(MemberStatus.HUMAN)
                .build();
        return oauthMember;
    }

    private static Map<String, Object> createData() {
        Map<String, Object> data =new HashMap<>();
        data.put("id","12356789");
        data.put("email","kms199719@naver.com");
        return data;
    }

    private static AuthenticationInfo getAuthenticationInfo() {
        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .name("name")
                .birth("19971202")
                .phoneNumber("01082460887")
                .build();
        return authenticationInfo;
    }

    private static MemberInfo getMemberInfo() {
        MemberInfo memberInfo = MemberInfo.builder()
                .companyName("safeking")
                .companyRegistrationNumber("111-12-12345")
                .corporateRegistrationNumber("111111-1234567")
                .representativeName("ms")
                .basicAddress("서울시")
                .detailedAddress("마포대로")
                .zipcode("111")
                .contact("01012345678")
                .build();
        return memberInfo;
    }
}