package com.safeking.shop.domain.user.web.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    String jwtToken=null;
    String refreshToken=null;
    @BeforeEach
    void init() throws Exception {
        userHelper.createMember();
        generateToken();
    }
    @Test
    @DisplayName("?????? ??????")
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
         * usingRecursiveComparison: ????????? ?????? ???????????? ?????? ??? ??????
         **/
        Assertions.assertAll(
                () -> assertThat(memberDetails.getName()).isEqualTo("user")
                , () -> assertThat(memberDetails.getUsername()).isEqualTo(TestUserHelper.USER_USERNAME)
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
                                fieldWithPath("name").description("??????")
                                ,fieldWithPath("username").description("?????? ID")
                                ,fieldWithPath("birth").description("??????")
                                ,fieldWithPath("representativeName").description("????????????")
                                ,fieldWithPath("phoneNumber").description("????????? ??????")
                                ,fieldWithPath("companyRegistrationNumber").description("????????? ?????? ??????")
                                ,fieldWithPath("corporateRegistrationNumber").description("?????? ?????? ??????")
                                ,fieldWithPath("basicAddress").description("?????? ??????")
                                ,fieldWithPath("detailedAddress").description("?????? ??????")
                                ,fieldWithPath("zipcode").description("?????? ??????")
                        )

                )
        );
    }
    @Test
    @DisplayName("?????? ??????")
    void update() throws Exception {
        //given
        String token = jwtToken;
        UpdateRequest updateRequest = UpdateRequest.builder()
                .name("nameChange")
                .birth("19971202")
                .representativeName("representativeNameChange")
                .phoneNumber("01082460887")
                .companyRegistrationNumber("111-12-12345")
                .corporateRegistrationNumber("111111-1234567")
                .basicAddress("?????????")
                .detailedAddress("????????????")
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
        Member member = memberRepository.findByUsername(TestUserHelper.USER_USERNAME).orElseThrow();

        assertAll(
                () -> assertThat(member.getName()).isEqualTo("nameChange")
                , () -> assertThat(member.getUsername()).isEqualTo(TestUserHelper.USER_USERNAME)
                , () -> assertThat(member.getBirth()).isEqualTo("19971202")
                , () -> assertThat(member.getRepresentativeName()).isEqualTo("representativeNameChange")
                , () -> assertThat(member.getPhoneNumber()).isEqualTo("01082460887")
                , () -> assertThat(member.getCompanyRegistrationNumber()).isEqualTo("111-12-12345")
                , () -> assertThat(member.getCorporateRegistrationNumber()).isEqualTo("111111-1234567")
                , () -> assertThat(
                        new Address(updateRequest.getBasicAddress()
                                , updateRequest.getDetailedAddress()
                                , updateRequest.getZipcode()))
                        .usingRecursiveComparison()
                        .isEqualTo(new Address("?????????"
                                , "????????????"
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
                                ,fieldWithPath("representativeName").attributes(InputValidation()).description("????????? ???")
                                ,fieldWithPath("phoneNumber").attributes(PhoneNumberValidation()).description("phoneNumber")
                                ,fieldWithPath("companyRegistrationNumber").attributes(companyRegistrationNumberValidation()).description("????????? ?????? ??????")
                                ,fieldWithPath("corporateRegistrationNumber").optional().attributes(InputValidation()).description("?????? ??????")
                                ,fieldWithPath("basicAddress").attributes(InputValidation()).description("?????? ??????")
                                ,fieldWithPath("detailedAddress").attributes(InputValidation()).description("?????? ??????")
                                ,fieldWithPath("zipcode").attributes(InputValidation()).description("?????? ??????")
                        )

                )
        );
    }

    @Test
    @DisplayName("?????? ???????????? ??????")
    void updatePassword() throws Exception {
        //given
        String token=jwtToken;

        UpdatePWRequest updatePWRequest = new UpdatePWRequest("password1234*");
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
                                fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                )
        );
    }
    @Test
    @DisplayName("????????? ???????????? ?????? ???")
    void MemberNotFound() throws Exception {
        //given
        String NoValidToken=jwtToken+"no";

        UpdatePWRequest updatePWRequest = new UpdatePWRequest("password1234*");
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
                                fieldWithPath("password").attributes(PWValidation()).description("password")
                        )
                )
        );
    }

    @Test
    @DisplayName("????????? ?????? ??????")
    void idDuplicationCheck() throws Exception {
        //given
        String token=jwtToken;

        IdDuplicationRequest idDuplicationRequest = new IdDuplicationRequest(TestUserHelper.USER_USERNAME);
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
                                fieldWithPath("username").attributes(IdValidation()).description("?????? ID")
                        )
                )
        );
    }

//    @Test
//    void idFind() throws Exception {
//        //given
//        PhoneNumber phoneNumber = new PhoneNumber("01082460887");
//        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());
//
//        IdFindRequest idFindRequest = new IdFindRequest(phoneNumber.getClientPhoneNumber(), code);
//        String content = om.writeValueAsString(idFindRequest);
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/find")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        //then
//        String result = resultActions.andReturn().getResponse().getContentAsString();
//
//        assertThat(result).isEqualTo(TestUserHelper.USER_USERNAME);
//        //docs
//        resultActions.andDo(
//                document("idFind"
//                        ,requestFields(
//                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("???????????? ??? ????????? ????????? ??????")
//                                ,fieldWithPath("code").description("????????? ?????? ???")
//                        )
//                )
//        );
//    }
//    @Test
//    @DisplayName("????????? ???????????? ?????? ??????")
//    void idFind_error1() throws Exception {
//        //given
//        PhoneNumber phoneNumber = new PhoneNumber("01082460887");
//        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());
//
//        IdFindRequest idFindRequest = new IdFindRequest(phoneNumber.getClientPhoneNumber(), "wrong");
//        String content = om.writeValueAsString(idFindRequest);
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/find")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//        //then
//        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
//        Error error = om.readValue(contentAsString, Error.class);
//
//        assertAll(
//                ()->assertThat(error.getCode()).isEqualTo(1300)
//        );
//        //docs
//        resultActions.andDo(
//                document("idFind_error1"
//                        ,requestFields(
//                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("???????????? ??? ????????? ????????? ??????")
//                                ,fieldWithPath("code").description("??????????????? code")
//                        )
//                        ,responseFields(
//                                fieldWithPath("code").description("error_code ??? 1300")
//                                ,fieldWithPath("message").description("error_message ??? ????????? ???????????? ????????????.")
//                        )
//                )
//        );
//    }
//    @Test
//    @DisplayName("??????????????? ????????? phoneNumber ??? ?????? ??????")
//    void idFind_error2() throws Exception {
//        //given
//        PhoneNumber phoneNumber = new PhoneNumber("01092460771");
//        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());
//
//        IdFindRequest idFindRequest = new IdFindRequest(phoneNumber.getClientPhoneNumber(), code);
//        String content = om.writeValueAsString(idFindRequest);
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/id/find")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//        //then
//        String contentAsString = resultActions.andReturn().getResponse().getContentAsString();
//        Error error = om.readValue(contentAsString, Error.class);
//
//        assertAll(
//                ()->assertThat(error.getCode()).isEqualTo(1200)
//        );
//        //docs
//        resultActions.andDo(
//                document("idFind_error2"
//                        ,requestFields(
//                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("???????????? ??? ????????? ????????? ????????? ?????? ??????")
//                                ,fieldWithPath("code").description("code")
//                        )
//                        ,responseFields(
//                                fieldWithPath("code").description("error_code ??? 1200")
//                                ,fieldWithPath("message").description("????????? ??????????????? ???????????? ????????? ????????????.")
//                        )
//                )
//        );
//    }

    @Test
    @DisplayName("?????? ???????????? ??????")
    void sendTemporaryPassword() throws Exception {
        //given
        PWFindRequest pwFindRequest = new PWFindRequest(TestUserHelper.USER_USERNAME);
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
                                fieldWithPath("username").attributes(IdValidation()).description("?????? ID")
                        )
                )
        );
    }
    @Test
    @DisplayName("?????? ????????? ??????")
    void showMemberList() throws Exception {
        //given
        //1) admin create
        userHelper.createADMIN();
        //2) member create
        for (int i = 1; i <= 30; i++) {
            Member user = GeneralMember.builder()
                    .name("user"+i)
                    .username("testUser" + i)
                    .password(encoder.encode("testUser" + i + "*"))
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .build();
            user.addLastLoginTime();
            memberRepository.save(user);
        }
        //3) admin login
        generateTokenAdmin();
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/member/list")
                        .header(AUTH_HEADER,jwtToken)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("name","user1")
                        .param("page","0")
                        .param("size","15"))
                .andExpect(status().isOk());
        //then
        /**
         * jsonPath ??? ???????????? json ????????? ?????? ????????? ??? ??????.
         **/
        resultActions
                .andExpect(jsonPath("content[0].name").value("user19"))
                .andExpect(jsonPath("totalPages").value("1"))
                .andExpect(jsonPath("totalElements").value("11"))
                .andExpect(jsonPath("size").value("15"))
                .andExpect(jsonPath("numberOfElements").value("11"))
        ;
        //docs
        resultActions.andDo(
                document("showMemberList"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestParameters(
                                parameterWithName("name").optional().description("?????? ??????")
                                ,parameterWithName("page").optional().description("page ??? 0?????? ??????")
                                ,parameterWithName("size").optional().description("size ??? ?????? 15")
                        )
                        ,responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY).description("?????? ?????????")
                                    ,fieldWithPath("content.[].memberId").type(JsonFieldType.NUMBER).description("?????? ID")
                                    ,fieldWithPath("content.[].name").description("?????? ??????")
                                    ,fieldWithPath("content.[].memberStatus").description("?????? ??????")

                                ,fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable ??????")

                                    ,fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("??????")
                                        ,fieldWithPath("pageable.sort.sorted").description("??????")
                                        ,fieldWithPath("pageable.sort.unsorted").description("??? ??????")
                                        ,fieldWithPath("pageable.sort.empty").description("empty")

                                    ,fieldWithPath("pageable.offset").description("offset")
                                    ,fieldWithPath("pageable.pageSize").description("?????? 15?????? ??????")
                                    ,fieldWithPath("pageable.pageNumber").description("????????? ??????")
                                    ,fieldWithPath("pageable.paged").description("paged")
                                    ,fieldWithPath("pageable.unpaged").description("unpaged")

                                ,fieldWithPath("last").description("????????? ????????? ??????")
                                ,fieldWithPath("totalElements").description("????????? ????????? ???")
                                ,fieldWithPath("totalPages").description("?????? ???????????? ???")
                                ,fieldWithPath("size").description("????????? ???????????? ????????? ?????? ?????? 15???")
                                ,fieldWithPath("number").description("????????? ??????")

                                ,fieldWithPath("sort").type(JsonFieldType.OBJECT).description("empty")
                                    ,fieldWithPath("sort.empty").description("empty")
                                    ,fieldWithPath("sort.sorted").description("??????")
                                    ,fieldWithPath("sort.unsorted").description("??? ??????")

                                ,fieldWithPath("first").description("?????? ????????? ??????")
                                ,fieldWithPath("numberOfElements").description("???????????? ????????? ??????")
                                ,fieldWithPath("empty").description("?????? ?????? ???")
                        )
                )
        );

    }
    @Test
    @DisplayName("refresh token ??????")
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
}