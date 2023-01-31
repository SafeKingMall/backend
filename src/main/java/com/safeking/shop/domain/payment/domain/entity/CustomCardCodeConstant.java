package com.safeking.shop.domain.payment.domain.entity;

import com.siot.IamportRestClient.constant.CardConstant;
import lombok.Getter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public final class CustomCardCodeConstant {
    /**
     * 361	BC카드
     * 364	광주카드
     * 365	삼성카드
     * 366	신한카드
     * 367	현대카드
     * 368	롯데카드
     * 369	수협카드
     * 370	씨티카드
     * 371	NH카드
     * 372	전북카드
     * 373	제주카드
     * 374	하나카드
     * 381	KB국민카드
     * 041	우리카드
     * 071	우체국
     * VIS	해외비자
     * MAS	해외마스터
     * DIN	해외다이너스
     * AMX	해외AMEX
     * JCB	해외JCB
     * UNI	중국은련
     */
    public static final String KR_CODE_BC = "BC카드";
    public static final String KR_CODE_KWANGJU = "광주카드";
    public static final String KR_CODE_SAMSUNG = "삼성카드";
    public static final String KR_CODE_SHINHAN = "신한카드";
    public static final String KR_CODE_HYUNDAI = "현대카드";
    public static final String KR_CODE_LOTTE = "롯데카드";
    public static final String KR_CODE_SUHYUP = "수협카드";
    public static final String KR_CODE_CITI = "씨티카드";
    public static final String KR_CODE_NH = "NH카드";
    public static final String KR_CODE_JEONBUK = "전북카드";
    public static final String KR_CODE_JEJU = "제주카드";
    public static final String KR_CODE_HANA = "하나카드";
    public static final String KR_CODE_KB = "KB국민카드";
    public static final String KR_CODE_WOORI = "우리카드";
    public static final String KR_CODE_EPOST = "우체국";
    public static final String KR_CODE_VISA = "해외비자";
    public static final String KR_CODE_MASTER = "해외마스터";
    public static final String KR_CODE_DINERS = "해외다이너스";
    public static final String KR_CODE_AMEX = "해외AMEX";
    public static final String KR_CODE_JCB = "해외JCB";
    public static final String KR_CODE_UNION = "중국은련";
}
