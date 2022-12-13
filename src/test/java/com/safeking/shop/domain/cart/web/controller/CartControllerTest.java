package com.safeking.shop.domain.cart.web.controller;

import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.request.BasicRequest;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.user.web.request.UpdatePWRequest;
import com.safeking.shop.global.CartHelper;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

import static com.safeking.shop.global.DocumentFormatGenerator.*;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static com.safeking.shop.global.jwt.TokenUtils.REFRESH_HEADER;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Transactional
class CartControllerTest extends MvcTest {
    @Autowired
    CartService cartService;
    @Autowired
    CartQueryRepository cartQueryRepository;
    @Autowired
    CartItemService cartItemService;
    @Autowired
    CartHelper cartHelper;
    @Autowired
    ItemRepository itemRepository;
    @Autowired
    CartItemRepository cartItemRepository;
    String jwtToken=null;
    List<Long> putItemIdList=null;

    @BeforeEach
    void init() throws Exception {
        putItemIdList = cartHelper.createTemporaryCartItem();
        generateToken();
    }

    @Test
    void putCart() throws Exception {
        //given
        Item item = itemRepository.save(new Item());
        BasicRequest basicRequest = new BasicRequest(item.getId(), 3);

        String content = om.writeValueAsString(basicRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        CartItem cartItem = cartItemRepository
                .findByItemIdAndUsername(item.getId(), TestUserHelper.USER_USERNAME).orElseThrow();

        assertThat(cartItem.getItem().getId()).isEqualTo(item.getId());
        assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo(TestUserHelper.USER_USERNAME);
        //docs
        resultActions.andDo(
                document("putCart"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("itemId").description("itemId")
                                ,fieldWithPath("count").attributes(CountValidation()).description("장바구니에 담는 아이템의 갯수")
                        )
                )
        );
    }

    @Test
    void updateCartItem() throws Exception {
        //given
        int updateCount=4;
        Long itemId=putItemIdList.get(0);

        BasicRequest basicRequest = new BasicRequest(itemId, updateCount);
        String content = om.writeValueAsString(basicRequest);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        CartItem cartItem = cartItemRepository
                .findByItemIdAndUsername(itemId, TestUserHelper.USER_USERNAME).orElseThrow();

        assertThat(cartItem.getItem().getId()).isEqualTo(itemId);
        assertThat(cartItem.getCart().getMember().getUsername()).isEqualTo(TestUserHelper.USER_USERNAME);
        assertThat(cartItem.getCount()).isEqualTo(updateCount);
        //docs
        resultActions.andDo(
                document("updateCartItem"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("itemId").description("itemId")
                                ,fieldWithPath("count").attributes(CountValidation()).description("수정시 장바구니에 담는 아이템의 갯수")
                        )
                )
        );
    }

    @Test
    void deleteCartItem() throws Exception {
        //given
        List<Long> itemIdList = putItemIdList;
        //when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .param("itemId", String.valueOf(itemIdList.get(0)))
                        .param("itemId", String.valueOf(itemIdList.get(1)))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
        //then
        assertThrows(NoSuchElementException.class,()->
                cartItemRepository.findByItemIdAndUsername(itemIdList.get(0), TestUserHelper.USER_USERNAME).orElseThrow());
        assertThrows(NoSuchElementException.class,()->
                cartItemRepository.findByItemIdAndUsername(itemIdList.get(1), TestUserHelper.USER_USERNAME).orElseThrow());
        //docs
        resultActions.andDo(
                document("deleteCartItem"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestParameters(
                                parameterWithName("itemId").optional().description("삭제할 itemId들")
                        )
                )
        );
    }

    @Test
    void showCartList() throws Exception {
        //given
        String token=jwtToken;
        //when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/user/cart")
                        .header(AUTH_HEADER, token)
                        .accept(MediaType.APPLICATION_JSON_UTF8)
                        .param("page","0")
                        .param("size","15"))
                .andExpect(status().isOk());
        //docs
        resultActions.andDo(
                document("showCartList"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestParameters(
                                parameterWithName("page").optional().description("page 는 0부터 시작")
                                ,parameterWithName("size").optional().description("size 는 기본 15")
                        )
                        ,responseFields(
                                fieldWithPath("content").type(JsonFieldType.ARRAY).description("장바구니 리스트")
                                    ,fieldWithPath("content.[].id").type(JsonFieldType.NUMBER).description("아이템 ID")
                                    ,fieldWithPath("content.[].itemName").description("아이템 이름")
                                    ,fieldWithPath("content.[].itemPrice").description("아이템 가격")
                                    ,fieldWithPath("content.[].itemQuantity").description("아이템 수량")
                                    ,fieldWithPath("content.[].categoryName").description("아이템 카테고리 이름")
                                    ,fieldWithPath("content.[].thumbNail").description("상품 썸네일")

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
    }
}