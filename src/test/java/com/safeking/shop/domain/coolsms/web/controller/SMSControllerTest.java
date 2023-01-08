//package com.safeking.shop.domain.coolsms.web.controller;
//
//import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
//import com.safeking.shop.domain.coolsms.web.request.PhoneNumber;
//import com.safeking.shop.domain.coolsms.web.request.SMSCode;
//import com.safeking.shop.global.MvcTest;
//import net.nurigo.java_sdk.exceptions.CoolsmsException;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.TestInstance;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.ResultActions;
//import org.springframework.transaction.annotation.Transactional;
//
//import static com.safeking.shop.global.DocumentFormatGenerator.*;
//import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
//import static org.junit.jupiter.api.Assertions.*;
//import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
//import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
//import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
//import static org.springframework.restdocs.payload.PayloadDocumentation.*;
//import static org.springframework.restdocs.snippet.Attributes.key;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
//@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@Transactional
//class SMSControllerTest extends MvcTest {
//    @Autowired
//    SMSService smsService;
//    @Test
//    @DisplayName("회원에게 code 전송")
//    void sendCodeToClient() throws Exception {
//        //given
//        PhoneNumber phoneNumber = new PhoneNumber("01022421190");
//
//        String content = om.writeValueAsString(phoneNumber);
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/coolsms")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk());
//        //then
//        resultActions.andDo(
//                document("sendCodeToClient"
//                        ,requestFields(
//                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("clientPhoneNumber")
//                        )
//                )
//        );
//
//    }
//
//    @Test
//    void checkCode() throws Exception {
//        //given
//        PhoneNumber phoneNumber = new PhoneNumber("01082460887");
//        String code = smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());
//
//        SMSCode smsCode = new SMSCode(phoneNumber.getClientPhoneNumber(), code);
//        String content = om.writeValueAsString(smsCode);
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/coolsms/code")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON));
//        //then
//        resultActions.andDo(
//                document("checkCode"
//                        ,requestFields(
//                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("clientPhoneNumber")
//                                ,fieldWithPath("code").attributes(key("format").value("비어있는 문자는 안됩니다.")).description("발급된 code")
//                        )
//                )
//        );
//    }
//    @Test
//    void checkCode_error() throws Exception {
//        //given
//        PhoneNumber phoneNumber = new PhoneNumber("01082460887");
//        smsService.sendCodeToClient(phoneNumber.getClientPhoneNumber());
//
//        SMSCode smsCode = new SMSCode(phoneNumber.getClientPhoneNumber(), "wrong");
//        String content = om.writeValueAsString(smsCode);
//        //when
//        ResultActions resultActions = mockMvc.perform(post("/api/v1/coolsms/code")
//                        .content(content)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isBadRequest());
//        //then
//        resultActions.andDo(
//                document("checkCode_error"
//                        ,requestFields(
//                                fieldWithPath("clientPhoneNumber").attributes(PhoneNumberValidation()).description("clientPhoneNumber")
//                                ,fieldWithPath("code").attributes(key("format").value("비어있는 문자는 안됩니다.")).description("틀린 code")
//                        )
//                        ,responseFields(
//                                fieldWithPath("code").description("error_code 는 1700")
//                                ,fieldWithPath("message").description("코드가 일치하지 않습니다.")
//                        )
//                )
//        );
//    }
//}