package com.safeking.shop.domain.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.web.request.signuprequest.CriticalItems;
import org.junit.Before;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static com.google.inject.util.Types.arrayOf;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.snippet.Attributes.key;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
@AutoConfigureRestDocs
//@ExtendWith({RestDocumentationExtension.class, SpringExtension.class}) // 문서 스니펫 생성을 위한 클래스
class MemberControllerTest_SignUp_docs {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;

    @Test
    @DisplayName("1. signUpCriticalItems")
    @Order(1)
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

        resultActions.andDo(
                        document("signUpCriticalItems"
                        ,preprocessRequest(prettyPrint())
                        ,preprocessResponse(prettyPrint())
                        ,requestFields(
                                fieldWithPath("username").description("username")
                                ,fieldWithPath("password").description("password")
                                ,fieldWithPath("email").type(JsonFieldType.STRING).attributes(key("format").value("이메일 형식이어야 합니다.")).description("email")
                                )
//                        ,requestHeaders(headerWithName("Authorization").description("Basic auth credentials"))
                        )

                );
    }



}