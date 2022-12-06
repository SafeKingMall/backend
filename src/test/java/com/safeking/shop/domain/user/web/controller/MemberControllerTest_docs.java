//package com.safeking.shop.domain.user.web.controller;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.safeking.shop.domain.user.domain.entity.Address;
//import com.safeking.shop.domain.user.domain.entity.MemberStatus;
//import com.safeking.shop.domain.user.domain.entity.RedisMember;
//import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
//import com.safeking.shop.domain.user.domain.entity.member.Member;
//import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
//import com.safeking.shop.domain.user.domain.repository.MemberRepository;
//import com.safeking.shop.domain.user.web.request.UpdateRequest;
//import com.safeking.shop.domain.user.web.response.MemberDetails;
//import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
//import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
//import org.junit.jupiter.api.*;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.mock.web.MockHttpServletResponse;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.transaction.annotation.Transactional;
//
//import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
//import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@ActiveProfiles("test")
//@SpringBootTest
//@AutoConfigureMockMvc
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
//@Transactional
//@AutoConfigureRestDocs
//class MemberControllerTest_docs {
//
//    @Autowired
//    MockMvc mockMvc;
//    @Autowired
//    ObjectMapper om;
//    @Autowired
//    CustomBCryPasswordEncoder encoder;
//    @Autowired
//    MemberRepository memberRepository;
//    @Autowired
//    MemberRedisRepository redisRepository;
//
//    String jwtToken;
//    String USERNAME="username";
//
//
//    @BeforeAll
//    void init(){
//        GeneralMember member = GeneralMember.builder()
//                .name("user")
//                .birth("birth")
//                .username(USERNAME)
//                .password(encoder.encode("password"))
//                .email("email")
//                .roles("ROLE_USER")
//                .phoneNumber("01012345678")
//                .companyName("safeking")
//                .companyRegistrationNumber("111")
//                .corporateRegistrationNumber("222")
//                .representativeName("MS")
//                .contact("contact")
//                .address(new Address("seoul", "mapo", "111"))
//                .agreement(true)
//                .accountNonLocked(true)
//                .status(MemberStatus.COMMON)
//                .build();
//        member.addLastLoginTime();
//        memberRepository.save(member);
//
//        memberRepository.findAll().stream()
//                .forEach(user -> redisRepository.save(new RedisMember(user.getRoles(),user.getUsername())));
//    }
//
//    @BeforeEach
//    void login_user() throws Exception {
//        //given
//        String content = om.writeValueAsString(
//                new LoginRequestDto(USERNAME, "password"));
//        //when
//        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andReturn().getResponse();
//        //then
//        assertThat(response.getStatus()).isEqualTo(200);
//        jwtToken=response.getHeader(AUTH_HEADER);
//    }
//
//    @Test
//    void showMemberDetails() throws Exception {
//        //given
//        String token=jwtToken;
//        //when
//        mockMvc.perform(get("/api/v1/user/details")
//                        .header(AUTH_HEADER,token))
//                .andExpect(status().isOk())
//                .andDo(
//                        document("user-userDetail"
//                        ,responseFields(
//                                fieldWithPath()
//                                )
//                        )
//                )
//
//    }
//
//
//}