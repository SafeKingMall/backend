package com.safeking.shop.domain.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.coolsms.web.request.PhoneNumber;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.web.request.*;
import com.safeking.shop.domain.user.web.response.MemberDetails;
import com.safeking.shop.domain.user.web.response.MemberListDto;
import com.safeking.shop.global.Error;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.sound.midi.Patch;

import static com.safeking.shop.global.DocumentFormatGenerator.*;
import static com.safeking.shop.global.TestUserHelper.*;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.REFRESH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class MemberControllerTest_Info extends MvcTest {

    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRedisRepository redisRepository;
    @Autowired
    TestUserHelper userHelper;
    @Autowired
    SMSService smsService;
    @Autowired
    CartService cartService;

    String jwtToken=null;
    String refreshToken=null;
    @BeforeEach
    void init() throws Exception {
        userHelper.createMember();
        generateToken();
    }
    @Test
    @DisplayName("회원 상세")
    void showMemberDetails() throws Exception {
        System.out.println("showMemberDetails");
        //given
        String token = jwtToken;
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/details")
                        .header(AUTH_HEADER, token))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();
        MemberDetails memberDetails = om.readValue(result, MemberDetails.class);
        /**
         * usingRecursiveComparison: 객체의 값을 비교하고 싶을 때 사용
         **/
        Assertions.assertAll(
                () -> assertThat(memberDetails.getName()).isEqualTo("user")
                , () -> assertThat(memberDetails.getUsername()).isEqualTo(USER_USERNAME)
                , () -> assertThat(memberDetails.getBirth()).isEqualTo("birth")
                , () -> assertThat(memberDetails.getRepresentativeName()).isEqualTo("MS")
                , () -> assertThat(memberDetails.getPhoneNumber()).isEqualTo("01082460887")
                , () -> assertThat(memberDetails.getCompanyRegistrationNumber()).isEqualTo("111")
                , () -> assertThat(memberDetails.getCorporateRegistrationNumber()).isEqualTo("222")
                , () -> assertThat(
                        new Address(memberDetails.getBasicAddress()
                                , memberDetails.getDetailedAddress()
                                , memberDetails.getZipcode()))
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("seoul", "mapo", "111"))
        );
        //docs
        resultActions.andDo(
                document("showMemberDetails"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,responseFields(
                                fieldWithPath("name").description("이름")
                                ,fieldWithPath("username").description("회원 ID")
                                ,fieldWithPath("birth").description("생일")
                                ,fieldWithPath("email").description("이메일")
                                ,fieldWithPath("representativeName").description("대표자명")
                                ,fieldWithPath("phoneNumber").description("핸드폰 번호")
                                ,fieldWithPath("companyRegistrationNumber").description("사업자 등록 번호")
                                ,fieldWithPath("corporateRegistrationNumber").description("법인 등록 번호")
                                ,fieldWithPath("basicAddress").description("기본 주소")
                                ,fieldWithPath("detailedAddress").description("상세 주소")
                                ,fieldWithPath("zipcode").description("우편 번호")
                        )

                )
        );
    }
    @Test
    @DisplayName("회원 탈퇴")
    void withdrawal() throws Exception {
        //given
        String token = jwtToken;

        cartService.createCart(
                memberRepository
                        .findByUsername(USER_USERNAME)
                        .orElseThrow());

        WithdrawalRequest withdrawalRequest = new WithdrawalRequest(USER_USERNAME, USER_PASSWORD);
        String content = om.writeValueAsString(withdrawalRequest);

        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/withdrawal")
                            .header(AUTH_HEADER, token)
                            .content(content)
                            .contentType(MediaType.APPLICATION_JSON)
                            .accept(MediaType.APPLICATION_JSON))
                            .andExpect(status().isOk());
        //docs
        resultActions.andDo(
                document("withdrawal"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("inputUsername").attributes(InputValidation()).description("user 가 작성한 아이디")
                                , fieldWithPath("password").attributes(InputValidation()).description("user 가 작성한 비밀번호")
                        )
                )
        );
    }
    @Test
    @DisplayName("회원 수정")
    void update() throws Exception {
        //given
        String token = jwtToken;
        UpdateRequest updateRequest = UpdateRequest.builder()
                .name("nameChange")
                .birth("19971202")
                .email("kms199710@naver.com")
                .representativeName("representativeNameChange")
                .phoneNumber("01082460887")
                .companyRegistrationNumber("111-12-12345")
                .corporateRegistrationNumber("111111-1234567")
                .basicAddress("서울시")
                .detailedAddress("마포대로")
                .zipcode("111")
                .build();
        String content = om.writeValueAsString(updateRequest);
        //when
        ResultActions resultActions = mockMvc.perform(put("/api/v1/user/update")
                .header(AUTH_HEADER, token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        Member member = memberRepository.findByUsername(USER_USERNAME).orElseThrow();

        assertAll(
                () -> assertThat(member.getName()).isEqualTo("nameChange")
                , () -> assertThat(member.getUsername()).isEqualTo(USER_USERNAME)
                , () -> assertThat(member.getBirth()).isEqualTo("19971202")
                , () -> assertThat(member.getEmail()).isEqualTo("kms199710@naver.com")
                , () -> assertThat(member.getRepresentativeName()).isEqualTo("representativeNameChange")
                , () -> assertThat(member.getPhoneNumber()).isEqualTo("01082460887")
                , () -> assertThat(member.getCompanyRegistrationNumber()).isEqualTo("111-12-12345")
                , () -> assertThat(member.getCorporateRegistrationNumber()).isEqualTo("111111-1234567")
                , () -> assertThat(
                        new Address(updateRequest.getBasicAddress()
                                , updateRequest.getDetailedAddress()
                                , updateRequest.getZipcode()))
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("서울시"
                                , "마포대로"
                                , "111"))
        );
        //docs
        resultActions.andDo(
                document("updateMemberDetails"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("name").attributes(InputValidation()).description("name")
                                ,fieldWithPath("birth").attributes(BirthValidation()).description("birth")
                                ,fieldWithPath("email").attributes(EmailValidation()).description("email")
                                ,fieldWithPath("representativeName").attributes(InputValidation()).description("대표자 명")
                                ,fieldWithPath("phoneNumber").attributes(PhoneNumberValidation()).description("phoneNumber")
                                ,fieldWithPath("companyRegistrationNumber").attributes(companyRegistrationNumberValidation()).description("사업자 등록 번호")
                                ,fieldWithPath("corporateRegistrationNumber").optional().attributes(InputValidation()).description("법인 번호")
                                ,fieldWithPath("basicAddress").attributes(InputValidation()).description("기본 주소")
                                ,fieldWithPath("detailedAddress").attributes(InputValidation()).description("상세 주소")
                                ,fieldWithPath("zipcode").attributes(InputValidation()).description("우편 번호")
                        )

                )
        );
    }

    @Test
    @DisplayName("회원 비밀번호 변경")
    void updatePassword() throws Exception {
        //given
        String token=jwtToken;

        UpdatePWRequest updatePWRequest = new UpdatePWRequest(USER_PASSWORD,"password1234*");
        String content = om.writeValueAsString(updatePWRequest);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user/update/password")
                .header(AUTH_HEADER, token)
                .content(content)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //docs
        resultActions.andDo(
                document("updatePassword"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("previousPassword").attributes().description("이전 비밀번호")
                                , fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                )
        );
    }
    @Test
    @DisplayName("토큰이 유효하지 않을 시")
    void MemberNotFound() throws Exception {
        //given
        String NoValidToken=jwtToken+"no";

        UpdatePWRequest updatePWRequest = new UpdatePWRequest(USER_PASSWORD,"password1234*");
        String content = om.writeValueAsString(updatePWRequest);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user/update/password")
                        .header(AUTH_HEADER, NoValidToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(result, Error.class);
        System.out.println("error.getMessage() = " + error.getMessage());
        System.out.println("error.getMessage() = " + error.getCode());
        //docs
        resultActions.andDo(
                document("MemberNotFound"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("previousPassword").attributes().description("이전 비밀번호")
                                , fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                )
        );
    }

    @Test
    @DisplayName("아이디 중복 검사")
    void idDuplicationCheck() throws Exception {
        //given
        String token=jwtToken;

        IdDuplicationRequest idDuplicationRequest = new IdDuplicationRequest(USER_USERNAME);
        String content = om.writeValueAsString(idDuplicationRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/duplication")
                        .header(AUTH_HEADER, token)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();
        assertThat(result).isEqualTo("false");
        //docs
        resultActions.andDo(
                document("idDuplicationCheck"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("회원 ID")
                        )
                )
        );
    }

    @Test
    void idFind() throws Exception {
        //given
        PhoneNumber phoneNumber = new PhoneNumber("01082460887");
        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());

        IdFindRequest idFindRequest = new IdFindRequest(phoneNumber.getClientPhoneNumber(), code);
        String content = om.writeValueAsString(idFindRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/find")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        String result = resultActions.andReturn().getResponse().getContentAsString();

        assertThat(result).isEqualTo(USER_USERNAME);
        //docs
        resultActions.andDo(
                document("idFind"
                        ,requestFields(
                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("회원가입 시 작성한 휴대폰 번호")
                                ,fieldWithPath("code").description("발송된 코드 값")
                        )
                )
        );
    }
    @Test
    @DisplayName("코드가 옳바르지 않는 경우")
    void idFind_error1() throws Exception {
        //given
        PhoneNumber phoneNumber = new PhoneNumber("01082460887");
        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());

        IdFindRequest idFindRequest = new IdFindRequest(phoneNumber.getClientPhoneNumber(), "wrong");
        String content = om.writeValueAsString(idFindRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/find")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        //then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(contentAsString, Error.class);

        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(1300)
        );
        //docs
        resultActions.andDo(
                document("idFind_error1"
                        ,requestFields(
                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("회원가입 시 작성한 휴대폰 번호")
                                ,fieldWithPath("code").description("잘못입력한 code")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 1300")
                                ,fieldWithPath("message").description("error_message 는 코드가 일치하지 않습니다.")
                        )
                )
        );
    }
    @Test
    @DisplayName("회원가입시 입력한 phoneNumber 와 다른 경우")
    void idFind_error2() throws Exception {
        //given
        PhoneNumber phoneNumber = new PhoneNumber("01092460771");
        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());

        IdFindRequest idFindRequest = new IdFindRequest(phoneNumber.getClientPhoneNumber(), code);
        String content = om.writeValueAsString(idFindRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/find")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
        //then
        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(contentAsString, Error.class);

        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(1200)
        );
        //docs
        resultActions.andDo(
                document("idFind_error2"
                        ,requestFields(
                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("회원가입 시 작성한 휴대폰 번호와 다른 번호")
                                ,fieldWithPath("code").description("code")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 1200")
                                ,fieldWithPath("message").description("등록된 휴대번호와 일치하는 회원이 없습니다.")
                        )
                )
        );
    }

    @Test
    @DisplayName("임시 비밀번호 발급")
    void sendTemporaryPassword() throws Exception {
        //given
        PWFindRequest pwFindRequest = new PWFindRequest(USER_USERNAME, EMAIL);
        String content = om.writeValueAsString(pwFindRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/temporaryPassword")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //docs
        resultActions.andDo(
                document("sendTemporaryPassword"
                        ,requestFields(
                                fieldWithPath("username").attributes(IdValidation()).description("회원 ID")
                                , fieldWithPath("email").attributes(EmailValidation()).description("이메일")
                        )
                )
        );
    }

    @Test
    @DisplayName("refresh token 발급")
    void refreshToken() throws Exception {
        //given
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/refresh")
                        .header(REFRESH_HEADER,refreshToken))
                .andExpect(status().isOk());
        //docs
        resultActions.andDo(
                document("refreshToken"
                        ,requestHeaders(
                                headerWithName(REFRESH_HEADER).attributes(RefreshTokenValidation()).description("refreshToken")
                        )
                        ,responseHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                                ,headerWithName(REFRESH_HEADER).attributes(RefreshTokenValidation()).description("refreshToken")
                        )
                )
        );
    }


    private void generateToken() throws Exception {
        String content = om.writeValueAsString(
                new LoginRequestDto(USER_USERNAME, USER_PASSWORD));
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
                new LoginRequestDto(ADMIN_USERNAME, ADMIN_PASSWORD));
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
}