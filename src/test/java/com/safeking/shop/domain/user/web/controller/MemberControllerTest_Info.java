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
                                fieldWithPath("name").description("이름")
                                ,fieldWithPath("username").description("회원 ID")
                                ,fieldWithPath("birth").description("생일")
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
    @DisplayName("회원 수정")
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
    @DisplayName("토큰이 유효하지 않을 시")
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
    @DisplayName("아이디 중복 검사")
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

        assertThat(result).isEqualTo(TestUserHelper.USER_USERNAME);
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
                                fieldWithPath("username").attributes(IdValidation()).description("회원 ID")
                        )
                )
        );
    }
    @Test
    @DisplayName("회원 리스트 조회")
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
         * jsonPath 를 사용하면 json 객체에 바로 다가갈 수 있다.
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
                                parameterWithName("name").optional().description("회원 이름")
                                ,parameterWithName("page").optional().description("page 는 0부터 시작")
                                ,parameterWithName("size").optional().description("size 는 기본 15")
                        )
                        ,responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY).description("회원 리스트")
                                    ,fieldWithPath("content.[].memberId").type(JsonFieldType.NUMBER).description("회원 ID")
                                    ,fieldWithPath("content.[].name").description("회원 이름")
                                    ,fieldWithPath("content.[].memberStatus").description("회원 상태")

                                ,fieldWithPath("pageable").type(JsonFieldType.OBJECT).description("pageable 정보")

                                    ,fieldWithPath("pageable.sort").type(JsonFieldType.OBJECT).description("정렬")
                                        ,fieldWithPath("pageable.sort.sorted").description("정렬")
                                        ,fieldWithPath("pageable.sort.unsorted").description("비 정렬")
                                        ,fieldWithPath("pageable.sort.empty").description("empty")

                                    ,fieldWithPath("pageable.offset").description("offset")
                                    ,fieldWithPath("pageable.pageSize").description("기본 15개로 설정")
                                    ,fieldWithPath("pageable.pageNumber").description("페이지 번호")
                                    ,fieldWithPath("pageable.paged").description("paged")
                                    ,fieldWithPath("pageable.unpaged").description("unpaged")

                                ,fieldWithPath("last").description("마지막 페이지 여부")
                                ,fieldWithPath("totalElements").description("전체의 데이터 수")
                                ,fieldWithPath("totalPages").description("전체 페이지의 수")
                                ,fieldWithPath("size").description("몇개의 데이터를 뿌릴지 여부 기본 15개")
                                ,fieldWithPath("number").description("페이지 번호")

                                ,fieldWithPath("sort").type(JsonFieldType.OBJECT).description("empty")
                                    ,fieldWithPath("sort.empty").description("empty")
                                    ,fieldWithPath("sort.sorted").description("정렬")
                                    ,fieldWithPath("sort.unsorted").description("비 정렬")

                                ,fieldWithPath("first").description("처음 페이지 인가")
                                ,fieldWithPath("numberOfElements").description("페이지의 데이터 갯수")
                                ,fieldWithPath("empty").description("비어 있는 가")
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