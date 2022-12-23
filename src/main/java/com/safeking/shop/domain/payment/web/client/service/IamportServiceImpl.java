package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentSuccessResponse;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamportServiceImpl implements IamportService {
    private final IamportClient client;
    private final String impUid = "imp28306430";

    /**
     * 결제 내역 단건 조회
     */
    @Override
    public IamportResponse<Payment> findPaymentByImpUid(String impUid) {
        IamportResponse<Payment> result = null;
        try {
            result = client.paymentByImpUid(impUid);
        } catch (IamportResponseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            // 서버 연결 끊김
            e.printStackTrace();
        }

        return result;
    }

    /**
     * 결제
     */
    @Override
    public PaymentResponse<PaymentSuccessResponse> payment(PaymentRequest paymentRequest) {
        /**
         * 1. 클라이언트 결제 요청
         * 2. 결제 단건 조회(아임포트)
         * 3. 결제 단건 조회(안전왕)
         */
        try {
            IamportResponse<Payment> paymentIamportResponse = client.paymentByImpUid(impUid);
            

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return null;
    }


}
