package com.safeking.shop.domain.payment.web.client.service;

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
}
