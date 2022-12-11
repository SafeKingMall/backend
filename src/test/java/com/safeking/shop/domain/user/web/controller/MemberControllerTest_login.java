package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.RedisService;
import com.safeking.shop.domain.user.domain.service.dto.CheckSignUp;
import com.safeking.shop.domain.user.web.request.UpdateRequest;
import com.safeking.shop.domain.user.web.request.signuprequest.AgreementInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.AuthenticationInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.MemberInfo;
import com.safeking.shop.domain.user.web.response.MemberDetails;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static com.safeking.shop.global.DocumentFormatGenerator.*;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@ActiveProfiles("test")
//@Transactional order 하고는 같이 사용 x rollback 이된다.
class MemberControllerTest_login extends MvcTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRedisRepository redisRepository;
    @Autowired
    TestUserHelper userHelper;
    @Autowired
    MemberService memberService;
    @Autowired
    RedisService redisService;
    String jwtToken=null;


    @Order(1)
    @Test
    void init(){
        userHelper.createMember();
        userHelper.createADMIN();
        userHelper.createDormant();

        redisService.deleteAll();
    }

    @Order(2)
    @DisplayName("login")
    @ParameterizedTest(name = "{index} {displayName}")
    @CsvSource({"testUser1234,testUser1234*,ROLE_USER","admin1234,admin1234*,ROLE_ADMIN"})
    void login(String username,String password, String role) throws Exception {
        //given
        String content = om.writeValueAsString(
                new LoginRequestDto(username, password));
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        MockHttpServletResponse response = resultActions.andReturn().getResponse();
        jwtToken = response.getHeader(AUTH_HEADER);
        //then
        RedisMember redisMember = redisRepository.findByUsername(username).orElseThrow();

        assertThat(redisMember.getUsername()).isEqualTo(username);
        assertThat(redisMember.getRoles()).isEqualTo(role);
        //docs
        resultActions.andDo(
                document("login"
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("username")
                                ,fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                )
        );

    }
    @Order(3)
    @DisplayName("dormant_login")
    @ParameterizedTest
    @CsvSource({"dormant1234,dormant1234*,ROLE_USER"})
    void login_dormant(String username,String password, String role) throws Exception {
        //given
        String content = om.writeValueAsString(
                new LoginRequestDto(username, password));
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
        //then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(contentAsString, Error.class);

        assertThat(error.getCode()).isEqualTo(1401);
        assertThat(error.getMessage()).isEqualTo("User account is locked");
        //docs
        resultActions.andDo(
                document("login_dormant"
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("username")
                                ,fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 1401")
                                ,fieldWithPath("message").description("error_message 는 User account is locked")
                        )
                )
        );

    }
    @Order(4)
    @DisplayName("아이디와 비밀번호가 맞지 않을 때")
    @ParameterizedTest
    @CsvSource({"wrong12345,wrong1234*,ROLE_USER"})
    void no_account_login(String username,String password, String role) throws Exception {
        //given
        String content = om.writeValueAsString(
                new LoginRequestDto(username, password));
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        //then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(contentAsString, Error.class);

        assertThat(error.getCode()).isEqualTo(1400);
        assertThat(error.getMessage()).isEqualTo("member not found");
        //docs
        resultActions.andDo(
                document("bad_login"
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("username")
                                ,fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 1400")
                                ,fieldWithPath("message").description("error_message 는 member not found")
                        )
                )
        );
    }

    @Order(5)
    @DisplayName("logout")
    @Test
    public void logout() throws Exception {
        //given
        String token=jwtToken;
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/logout")
                        .header(AUTH_HEADER, token))
                .andExpect(status().isOk());
        //then
        assertThrows(NoSuchElementException.class,()
                ->redisRepository.findByUsername(TestUserHelper.ADMIN_USERNAME).orElseThrow());
        //docs
        resultActions.andDo(
                document("logout"
                        ,requestHeaders(
                            headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                )
        );


    }
    @Order(6)
    @DisplayName("rollback")
    @Test
    public void rollback(){
        List<Member> all = memberRepository.findAll();
        for (Member member : all) {
            memberRepository.delete(member);
        }
        redisService.deleteAll();
    }
    @Test
    @DisplayName("소셜 로그인")
    public void socialLogin() throws Exception {
        //social signup
        String registrationId="kakao";
        Map<String, Object> data =new HashMap<>();
        data.put("id","123456789");
        data.put("email","kms199719@naver.com");
        CheckSignUp checkSignUp = memberService.socialLogin(registrationId, data);

        AuthenticationInfo authenticationInfo = AuthenticationInfo.builder()
                .name("name")
                .birth("20001202")
                .phoneNumber("01082460887")
                .build();
        Long memberId = memberService.addAuthenticationInfo(checkSignUp.getId(), authenticationInfo.toServiceDto());

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
        Long memberId2 = memberService.addMemberInfo(memberId, memberInfo.toServiceDto());

        AgreementInfo agreementInfo = AgreementInfo.builder()
                .userAgreement(true)
                .infoAgreement(true)
                .build();
        memberService.changeMemoryToDB(memberId2,true);

        //given
        String content = om.writeValueAsString(data);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.post("/api/v1/oauth/{registrationId}",registrationId)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        //then
        RedisMember redisMember = redisRepository.findByUsername(registrationId+"_"+"123456789").orElseThrow();

        assertThat(redisMember.getUsername()).isEqualTo(registrationId+"_"+"123456789");
        assertThat(redisMember.getRoles()).isEqualTo("ROLE_USER");
        //docs
        resultActions.andDo(
                document("socialLogin"
                        ,pathParameters(
                                parameterWithName("registrationId").description("kakao or google")
                        )
                        ,requestFields(
                                fieldWithPath("id").attributes(IdValidation()).description("카카오 일시는 id, 구글일 시에는 sub")
                                ,fieldWithPath("email").attributes(EmailValidation()).description("email")
                        )
                )
        );
        redisService.deleteAll();

    }



}