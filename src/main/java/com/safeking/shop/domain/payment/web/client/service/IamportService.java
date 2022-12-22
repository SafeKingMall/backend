package com.safeking.shop.domain.payment.web.client.service;

import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;

public interface IamportService {
    //결제내역 단건 조회
    IamportResponse<Payment> findPaymentByImpUid(String impUid);
    // 결제 취소
    // 결제내역 복수 조회
    // 결제상태 기준 복수 조회
}
