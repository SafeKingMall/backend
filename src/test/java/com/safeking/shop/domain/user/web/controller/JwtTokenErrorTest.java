package com.safeking.shop.domain.user.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;

import static com.safeking.shop.global.DocumentFormatGenerator.JwtTokenValidation;
import static com.safeking.shop.global.DocumentFormatGenerator.RefreshTokenValidation;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.REFRESH_HEADER;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class JwtTokenErrorTest  extends MvcTest {
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRedisRepository redisRepository;
    @Autowired
    TestUserHelper userHelper;

    String jwtToken=null;
    String refreshToken=null;
    @BeforeEach
    void init() throws Exception {
        userHelper.createMember();
        generateToken();
    }
    @Test
    @DisplayName("JWT 양식이 잘못 됫을 때")
    void JWTDecodeException() throws Exception {
        //given
        String wrongToken="Bearer sssegeroiwgjwoijgow";
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/details")
                        .header(AUTH_HEADER,wrongToken))
                .andExpect(status().isUnauthorized());
        //then
        Error error = getError(resultActions);
        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(811)
                ,()->assertThat(error.getMessage()).isEqualTo("잘못된 jwt 양식입니다.")
        );
        //docs
        resultActions.andDo(
                document("JWTDecodeException"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwt 양식이 잘못된 token")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 811")
                                ,fieldWithPath("message").description("error_message 는 잘못된 jwt 양식입니다.")
                        )
                )
        );
    }



    @Test
    @DisplayName("JWT 서명이 잘못 됫을 때")
    void SignatureVerificationException() throws Exception {
        //given
        String wrongToken=jwtToken+"!!";
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/details")
                        .header(AUTH_HEADER,wrongToken))
                .andExpect(status().isUnauthorized());
        //then
        Error error = getError(resultActions);
        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(813)
                ,()->assertThat(error.getMessage()).isEqualTo("잘못된 서명입니다.")
        );
        //docs
        resultActions.andDo(
                document("SignatureVerificationException"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwt 양식이 잘못된 token")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 813")
                                ,fieldWithPath("message").description("error_message 는 잘못된 서명입니다.")
                        )
                )
        );
    }
    @Test
    @DisplayName("권한처리")
    void no_authority() throws Exception {
        //given
        String wrongToken=jwtToken;
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/member/list")
                        .header(AUTH_HEADER,wrongToken))
                .andExpect(status().isForbidden());
        //docs
        resultActions.andDo(
                document("no_authority"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("권한처리에 맞지 않는 jwtToken")
                        )
                )
        );
    }
    @Test
    @DisplayName("토큰이 없을 때")
    void no_token() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/admin/member/list"))
                .andExpect(status().isForbidden());
        //docs
        resultActions.andDo(
                document("no_token")
        );
    }



    private void generateToken() throws Exception {
        String content = om.writeValueAsString(
                new LoginRequestDto(TestUserHelper.USER_USERNAME, TestUserHelper.USER_PASSWORD));
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(200);
        jwtToken = response.getHeader(AUTH_HEADER);
        refreshToken = response.getHeader(REFRESH_HEADER);
    }
    private void generateTokenAdmin() throws Exception {
        String content = om.writeValueAsString(
                new LoginRequestDto(TestUserHelper.ADMIN_USERNAME, TestUserHelper.ADMIN_PASSWORD));
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(200);
        jwtToken = response.getHeader(AUTH_HEADER);
    }
    private Error getError(ResultActions resultActions) throws UnsupportedEncodingException, JsonProcessingException {
        String result = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(result, Error.class);
        return error;
    }
}
