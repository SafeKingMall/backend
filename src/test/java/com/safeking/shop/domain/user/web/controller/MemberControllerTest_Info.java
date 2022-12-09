package com.safeking.shop.domain.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.web.request.UpdateRequest;
import com.safeking.shop.domain.user.web.response.MemberDetails;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.RestDocsConfiguration;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MemberControllerTest_Info extends MvcTest {

    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRedisRepository redisRepository;
    @Autowired
    TestUserHelper userHelper;

    String jwtToken=null;
    String USERNAME = "username";


    @BeforeEach
    void init() throws Exception {
        userHelper.createMember();
        generateToken();
    }
    @Test
    void showMemberDetails() throws Exception {
        System.out.println("showMemberDetails");
        //given
        String token = jwtToken;
        //when
        String responseData = mockMvc.perform(get("/api/v1/user/details")
                        .header(AUTH_HEADER, token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //then
        MemberDetails memberDetails = om.readValue(responseData, MemberDetails.class);

        Assertions.assertAll(
                () -> assertThat(memberDetails.getName()).isEqualTo("user")
                , () -> assertThat(memberDetails.getUsername()).isEqualTo("username")
                , () -> assertThat(memberDetails.getBirth()).isEqualTo("birth")
                , () -> assertThat(memberDetails.getRepresentativeName()).isEqualTo("MS")
                , () -> assertThat(memberDetails.getPhoneNumber()).isEqualTo("01012345678")
                , () -> assertThat(memberDetails.getCompanyRegistrationNumber()).isEqualTo("111")
                , () -> assertThat(memberDetails.getCorporateRegistrationNumber()).isEqualTo("222")
                , () -> assertThat(
                        new Address(memberDetails.getBasicAddress()
                                , memberDetails.getDetailedAddress()
                                , memberDetails.getZipcode()))
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("seoul", "mapo", "111"))
        );
    }

    @Test
    void update() throws Exception {
        //given
        String token = jwtToken;
        UpdateRequest updateRequest = UpdateRequest.builder()
                .name("nameChange")
                .birth("birthChange")
                .representativeName("representativeNameChange")
                .phoneNumber("01082460887")
                .companyRegistrationNumber("companyRegistrationNumberChange")
                .corporateRegistrationNumber("corporateRegistrationNumberChange")
                .basicAddress("basicAddressChange")
                .detailedAddress("detailedAddressChange")
                .zipcode("zipcodeChange")
                .build();
        String content = om.writeValueAsString(updateRequest);
        //when
        mockMvc.perform(put("/api/v1/user/update")
                        .header(AUTH_HEADER, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();
        //then
        Member member = memberRepository.findByUsername(USERNAME).orElseThrow();

        assertAll(
                () -> assertThat(member.getName()).isEqualTo("nameChange")
                , () -> assertThat(member.getUsername()).isEqualTo("username")
                , () -> assertThat(member.getBirth()).isEqualTo("birthChange")
                , () -> assertThat(member.getRepresentativeName()).isEqualTo("representativeNameChange")
                , () -> assertThat(member.getPhoneNumber()).isEqualTo("01082460887")
                , () -> assertThat(member.getCompanyRegistrationNumber()).isEqualTo("companyRegistrationNumberChange")
                , () -> assertThat(member.getCorporateRegistrationNumber()).isEqualTo("corporateRegistrationNumberChange")
                , () -> assertThat(
                        new Address(updateRequest.getBasicAddress()
                                , updateRequest.getDetailedAddress()
                                , updateRequest.getZipcode()))
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("basicAddressChange"
                                , "detailedAddressChange"
                                , "zipcodeChange"))
        );
    }

    @Test
    void updatePassword() {
    }

    @Test
    void idDuplicationCheck() {
    }

    @Test
    void idFind() {
    }

    @Test
    void sendTemporaryPassword() {
    }

    @Test
    void showMemberList() {
    }

    @Test
    void humanConverterBatch() {
    }

    @Test
    void socialLogin() {
    }

    private void generateToken() throws Exception {
        String content = om.writeValueAsString(
                new LoginRequestDto(TestUserHelper.USERNAME, TestUserHelper.PASSWORD));

        System.out.println("login begin");
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        System.out.println("login end");

        assertThat(response.getStatus()).isEqualTo(200);
        jwtToken = response.getHeader(AUTH_HEADER);
    }
}