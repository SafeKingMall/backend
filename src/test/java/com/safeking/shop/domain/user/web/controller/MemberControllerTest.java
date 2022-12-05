package com.safeking.shop.domain.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.web.request.signuprequest.CriticalItems;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
@AutoConfigureMockMvc
class MemberControllerTest_SignUp {

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
        String returnData = mockMvc.perform(post("/api/v1/signup/criticalItems")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        //then
        System.out.println("returnData = " + returnData);
    }

    @Test
    void signUpAuthenticationInfo() {
    }

    @Test
    void signUpMemberInfo() {
    }

    @Test
    void signUpAgreementInfo() {
    }

    @Test
    void memoryMemberRepoClear() {
    }
}