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
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static com.safeking.shop.global.jwt.TokenUtils.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Transactional
class MemberControllerTest_Info {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper om;
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRedisRepository redisRepository;

    String jwtToken;
    String USERNAME="username";


    @BeforeAll
    void init(){
        GeneralMember member = GeneralMember.builder()
                .name("user")
                .birth("birth")
                .username(USERNAME)
                .password(encoder.encode("password"))
                .email("email")
                .roles("ROLE_USER")
                .phoneNumber("01012345678")
                .companyName("safeking")
                .companyRegistrationNumber("111")
                .corporateRegistrationNumber("222")
                .representativeName("MS")
                .contact("contact")
                .address(new Address("seoul", "mapo", "111"))
                .agreement(true)
                .accountNonLocked(true)
                .status(MemberStatus.COMMON)
                .build();
        member.addLastLoginTime();
        memberRepository.save(member);

        memberRepository.findAll().stream()
                .forEach(user -> redisRepository.save(new RedisMember(user.getRoles(),user.getUsername())));
    }

    @BeforeEach
    void login_user() throws Exception {
        //given
        String content = om.writeValueAsString(
                new LoginRequestDto(USERNAME, "password"));
        //when
        MockHttpServletResponse response = mockMvc.perform(post("/api/v1/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andReturn().getResponse();
        //then
        assertThat(response.getStatus()).isEqualTo(200);
        jwtToken=response.getHeader(AUTH_HEADER);
    }

    @Test
    void showMemberDetails() throws Exception {
        //given
        String token=jwtToken;
        //when
        String responseData = mockMvc.perform(get("/api/v1/user/details")
                        .header(AUTH_HEADER,token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        //then
        MemberDetails memberDetails = om.readValue(responseData, MemberDetails.class);

        Assertions.assertAll(
                ()->assertThat(memberDetails.getName()).isEqualTo("user")
                ,()->assertThat(memberDetails.getUsername()).isEqualTo("username")
                ,()->assertThat(memberDetails.getBirth()).isEqualTo("birth")
                ,()->assertThat(memberDetails.getRepresentativeName()).isEqualTo("MS")
                ,()->assertThat(memberDetails.getPhoneNumber()).isEqualTo("01012345678")
                ,()->assertThat(memberDetails.getCompanyRegistrationNumber()).isEqualTo("111")
                ,()->assertThat(memberDetails.getCorporateRegistrationNumber()).isEqualTo("222")
                ,()->assertThat(
                        new Address(memberDetails.getBasicAddress()
                        ,memberDetails.getDetailedAddress()
                        ,memberDetails.getZipcode()))
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("seoul", "mapo", "111"))
        );
    }

    @Test
    void update() throws Exception {
        //given
        String token=jwtToken;
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
                ()->assertThat(member.getName()).isEqualTo("nameChange")
                ,()->assertThat(member.getUsername()).isEqualTo("username")
                ,()->assertThat(member.getBirth()).isEqualTo("birthChange")
                ,()->assertThat(member.getRepresentativeName()).isEqualTo("representativeNameChange")
                ,()->assertThat(member.getPhoneNumber()).isEqualTo("01082460887")
                ,()->assertThat(member.getCompanyRegistrationNumber()).isEqualTo("companyRegistrationNumberChange")
                ,()->assertThat(member.getCorporateRegistrationNumber()).isEqualTo("corporateRegistrationNumberChange")
                ,()->assertThat(
                        new Address(updateRequest.getBasicAddress()
                                ,updateRequest.getDetailedAddress()
                                ,updateRequest.getZipcode()))
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
}