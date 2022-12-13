package com.safeking.shop.domain.cart.web.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.safeking.shop.domain.cart.domain.entity.CartItem;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.service.CartItemService;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.cart.web.query.repository.CartQueryRepository;
import com.safeking.shop.domain.cart.web.request.BasicRequest;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.global.CartHelper;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.MvcTest;
import com.safeking.shop.global.TestUserHelper;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
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

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.NoSuchElementException;

import static com.safeking.shop.global.DocumentFormatGenerator.CountValidation;
import static com.safeking.shop.global.DocumentFormatGenerator.JwtTokenValidation;
import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
class CartControllerErrorTest extends MvcTest {
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
    @DisplayName("아이템이 없을 시")
    void putCart_error1() throws Exception {
        //given
        BasicRequest basicRequest = new BasicRequest(115L, 3);

        String content = om.writeValueAsString(basicRequest);
        //when
        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        //then
        Error error = getError(resultActions);

        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(300)
        );
        //docs
        resultActions.andDo(
                document("putCart_error1"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("itemId").description("존재하지 않는 아이템 id")
                                ,fieldWithPath("count").attributes(CountValidation()).description("장바구니에 담는 아이템의 갯수")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 300")
                                ,fieldWithPath("message").description("error_message 는 아이템이 없습니다.")
                        )
                )
        );
    }
    @Test
    @DisplayName("동일한 아이템이 장바구니에 있는 경우")
    void putCart_error2() throws Exception {
        //given
        Item item = itemRepository.save(new Item());
        BasicRequest basicRequest = new BasicRequest(item.getId(), 3);

        String content = om.writeValueAsString(basicRequest);
        //when
        mockMvc.perform(post("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        ResultActions resultActions = mockMvc.perform(post("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());

        //then
        Error error = getError(resultActions);
        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(301)
        );
        //docs
        resultActions.andDo(
                document("putCart_error2"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("itemId").description("동일한 아이템 id")
                                ,fieldWithPath("count").attributes(CountValidation()).description("장바구니에 담는 아이템의 갯수")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 301")
                                ,fieldWithPath("message").description("동일한 상품이 장바구니에 있습니다.")
                        )
                )
        );
    }

    @Test
    void updateCartItem_error() throws Exception {
        //given
        int updateCount=4;
        Long itemId=putItemIdList.get(0)+1;
        BasicRequest basicRequest = new BasicRequest(itemId, updateCount);

        String content = om.writeValueAsString(basicRequest);
        //when
        ResultActions resultActions = mockMvc.perform(RestDocumentationRequestBuilders.patch("/api/v1/user/cartItem")
                        .header(AUTH_HEADER, jwtToken)
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError());
        //then
        Error error = getError(resultActions);
        assertAll(
                ()->assertThat(error.getCode()).isEqualTo(300)
        );
        //docs
        resultActions.andDo(
                document("updateCartItem_error"
                        ,requestHeaders(
                                headerWithName(AUTH_HEADER).attributes(JwtTokenValidation()).description("jwtToken")
                        )
                        ,requestFields(
                                fieldWithPath("itemId").description("존재하지 않는 아이템 id")
                                ,fieldWithPath("count").attributes(CountValidation()).description("장바구니에 담는 아이템의 갯수")
                        )
                        ,responseFields(
                                fieldWithPath("code").description("error_code 는 300")
                                ,fieldWithPath("message").description("error_message 는 아이템이 없습니다.")
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
    private Error getError(ResultActions resultActions) throws UnsupportedEncodingException, JsonProcessingException {
        String result = resultActions.andReturn().getResponse().getContentAsString();
        Error error = om.readValue(result, Error.class);
        return error;
    }
}