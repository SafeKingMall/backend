package com.safeking.shop.domain.payment.domain.repository;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.safeking.shop.domain.payment.domain.entity.CustomCardCodeConstant.*;
import static com.safeking.shop.domain.payment.domain.entity.CustomPayMethodConstant.*;
import static com.siot.IamportRestClient.constant.CardConstant.*;

@Component
public class CustomPaymentRepository {
    private final static Map<String, String> cardCodeMap = new ConcurrentHashMap<>();
    private final static Map<String, String> payMethodMap = new ConcurrentHashMap<>();

    @PostConstruct
    public static void init() {
        cardCodeMap.put(CODE_BC, KR_CODE_BC);
        cardCodeMap.put(CODE_KWANGJU, KR_CODE_KWANGJU);
        cardCodeMap.put(CODE_SAMSUNG, KR_CODE_SAMSUNG);
        cardCodeMap.put(CODE_SHINHAN, KR_CODE_SHINHAN);
        cardCodeMap.put(CODE_HYUNDAI, KR_CODE_HYUNDAI);
        cardCodeMap.put(CODE_LOTTE, KR_CODE_LOTTE);
        cardCodeMap.put(CODE_SUHYUP, KR_CODE_SUHYUP);
        cardCodeMap.put(CODE_CITI, KR_CODE_CITI);
        cardCodeMap.put(CODE_NH, KR_CODE_NH);
        cardCodeMap.put(CODE_JEONBUK, KR_CODE_JEONBUK);
        cardCodeMap.put(CODE_JEJU, KR_CODE_JEJU);
        cardCodeMap.put(CODE_HANA, KR_CODE_HANA);
        cardCodeMap.put(CODE_KB, KR_CODE_KB);
        cardCodeMap.put(CODE_WOORI, KR_CODE_WOORI);
        cardCodeMap.put(CODE_EPOST, KR_CODE_EPOST);
        cardCodeMap.put(CODE_VISA, KR_CODE_VISA);
        cardCodeMap.put(CODE_MASTER, KR_CODE_MASTER);
        cardCodeMap.put(CODE_DINERS, KR_CODE_DINERS);
        cardCodeMap.put(CODE_AMEX, KR_CODE_AMEX);
        cardCodeMap.put(CODE_JCB, KR_CODE_JCB);
        cardCodeMap.put(CODE_UNION, KR_CODE_UNION);

        payMethodMap.put(SAMSUNG, KR_SAMSUNG);
        payMethodMap.put(CARD, KR_CARD);
        payMethodMap.put(TRANS, KR_TRANS);
        payMethodMap.put(VBANK, KR_VBANK);
        payMethodMap.put(PHONE, KR_SAMSUNG);
        payMethodMap.put(CULTURELAND, KR_CULTURELAND);
        payMethodMap.put(SMARTCULTURE, KR_SMARTCULTURE);
        payMethodMap.put(BOOKLIFE, KR_BOOKLIFE);
        payMethodMap.put(HAPPYMONEY, KR_HAPPYMONEY);
        payMethodMap.put(POINT, KR_POINT);
        payMethodMap.put(SSGPAY, KR_SSGPAY);
        payMethodMap.put(LPAY, KR_LPAY);
        payMethodMap.put(PAYCO, KR_PAYCO);
        payMethodMap.put(KAKAOPAY, KR_KAKAOPAY);
        payMethodMap.put(TOSSPAY, KR_TOSSPAY);
        payMethodMap.put(NAVERPAY, KR_NAVERPAY);
    }

    public static String getCardCodeElement(String key) {
        return cardCodeMap.get(key);
    }

    public static String getPayMethodElement(String key) {
        return payMethodMap.get(key);
    }
}
