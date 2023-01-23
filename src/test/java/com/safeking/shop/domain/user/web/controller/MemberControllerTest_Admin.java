package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.assertj.core.api.Assertions;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.ArrayList;

import static com.safeking.shop.domain.order.domain.entity.Order.createOrder;
import static com.safeking.shop.global.DocumentFormatGenerator.JwtTokenValidation;
import static com.safeking.shop.global.TestUserHelper.*;
import static com.safeking.shop.global.TestUserHelper.ADMIN_PASSWORD;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.REFRESH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
public class MemberControllerTest_Admin extends MvcTest {
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
    @Autowired
    EntityManager em;

    String jwtToken=null;

    @Test
    @DisplayName("회원 탈퇴")
    void withdrawalByAdmin() throws Exception {
        //given
        //1) admin create
        userHelper.createADMIN();
        //2) member create
        Member member = userHelper.createMember();

        //3) login
        generateToken();
        generateTokenAdmin();

        // when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/withdrawal/{memberId}", member.getId())
                        .header(AUTH_HEADER,jwtToken)
                        .accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
        em.flush();
        em.clear();
        // then
        Member findMember = memberRepository.findById(member.getId()).orElseThrow();

        assertAll(
                () -> assertThat(findMember.getStatus()).isEqualTo(MemberStatus.WITHDRAWAL)
                , () -> assertThat(findMember.getAccountNonLocked()).isEqualTo(false)
        );
        // docs
        resultActions.andDo(
                document("withdrawalByAdmin"
                        ,pathParameters(
                                parameterWithName("memberId").description("탈퇴 시킬 회원 아이디")
                        )
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                ));
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
                        .param("status","COMMON")
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
                                ,parameterWithName("status").optional().description("회원 상태")
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
    @DisplayName("탈퇴회원 리스트 조회")
    void showWithDrawlList() throws Exception {
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
        //2-1) 탈퇴 회원을 생성
        GeneralMember withDrawlMember = GeneralMember.builder()
                .name("withDrawl")
                .username("withDrawl1234")
                .password(encoder.encode("withDrawl1234*"))
                .accountNonLocked(false)
                .status(MemberStatus.WITHDRAWAL)
                .build();
        withDrawlMember.addLastLoginTime();
        memberRepository.save(withDrawlMember);

        //3) admin login
        generateTokenAdmin();
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.get("/api/v1/admin/member/withDrawlList")
                        .header(AUTH_HEADER,jwtToken)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("name","withDrawl")
                        .param("page","0")
                        .param("size","15"))
                .andExpect(status().isOk());
        //then
        /**
         * jsonPath 를 사용하면 json 객체에 바로 다가갈 수 있다.
         **/

        resultActions
                .andExpect(jsonPath("content[0].name").value("withDrawl"))
                .andExpect(jsonPath("totalPages").value("1"))
                .andExpect(jsonPath("totalElements").value("1"))
                .andExpect(jsonPath("size").value("15"))
                .andExpect(jsonPath("numberOfElements").value("1"))
        ;
        //docs
        resultActions.andDo(
                document("showWithDrawlList"
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
                                ,fieldWithPath("content.[].withdrawalDate").description("회원 탈퇴 일자")

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
    }
}
