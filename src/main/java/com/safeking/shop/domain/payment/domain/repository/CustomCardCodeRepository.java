package com.safeking.shop.domain.payment.domain.repository;

import com.safeking.shop.domain.payment.domain.entity.CustomCardCodeConstant;
import com.siot.IamportRestClient.constant.CardConstant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.annotation.PostConstruct;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.safeking.shop.domain.payment.domain.entity.CustomCardCodeConstant.*;
import static com.safeking.shop.domain.payment.domain.entity.CustomCardCodeConstant.KR_CODE_LOTTE;
import static com.siot.IamportRestClient.constant.CardConstant.*;

@Repository
public class CustomCardCodeRepository {
    private final static Map<String, String> cardCodeMap = new ConcurrentHashMap<>();

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
    }

    public static String getElement(String key) {
        return cardCodeMap.get(key);
    }
}
