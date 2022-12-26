package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
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

    /**
     * 결제
     */
    @Override
    public PaymentResponse<PaymentSuccessResponse> paymentByCallback(PaymentRequest request) {
        /**
         * 1. 클라이언트 결제 요청
         * 2. 결제 단건 조회(아임포트)
         * 3. 결제 단건 조회(안전왕)
         * imp_655785233420
         */

        PaymentResponse<PaymentSuccessResponse> response = new PaymentResponse<>();

        try {
            // 결제 단건 조회(아임포트)
            IamportResponse<Payment> iamportResponse = client.paymentByImpUid(request.getImpUid());

            // 결제 금액 일치
            if(request.getPaidAmount() == iamportResponse.getResponse().getAmount().intValue()) {
                switch (iamportResponse.getResponse().getStatus()) {
                    case "ready" : break;
                    case "paid" :
                        response.setMessage("일반 결제 성공");
                        response.setResponse(PaymentSuccessResponse.builder()
//                                        .payMethod(request.getPayMethod())
//                                        .merchantUid(request.getMerchantUid())
//                                        .amount(request.getAmount())
//                                        .buyerAddr(request.getBuyerAddr())
//                                        .buyerEmail(request.getBuyerEmail())
//                                        .buyerName(request.getBuyerName())
//                                        .buyerPostcode(request.getBuyerPostcode())
//                                        .buyerTel(request.getBuyerTel())
//                                        .name(request.getName())
                                        .merchantUid(request.getMerchantUid())
                                        .build());
                        break;
                    case "cancelled" : break;
                    case "failed" : break;
                }
            } else {
                response.setMessage("위조된 결제 시도");
                return response;
            }

        } catch (IamportResponseException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @Override
    public PaymentResponse<PaymentSuccessResponse> paymentByWebhook(PaymentWebhookRequest request) {
        return null;
    }


}
