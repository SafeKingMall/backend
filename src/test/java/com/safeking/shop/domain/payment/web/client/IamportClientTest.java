package com.safeking.shop.domain.payment.web.client;

import com.safeking.shop.domain.payment.web.client.service.IamportService;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.AccessToken;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class IamportClientTest {
    @Autowired
    IamportClient client;
    @Autowired
    IamportService iamportService;
    final String impUid = "imp28306430";

    @Test
    void testGetToken() {
        try {
            IamportResponse<AccessToken> tokenIamportResponse = client.getAuth();
            assertThat(tokenIamportResponse.getResponse()).isNotNull();
            assertThat(tokenIamportResponse.getResponse().getToken()).isNotNull();
            System.out.println(tokenIamportResponse.getResponse().getToken());

        } catch (IamportResponseException e) {
            System.out.println(e.getMessage());

            switch (e.getHttpStatusCode()) {
                case 401:
                    System.out.println(e.getHttpStatusCode());
                    break;
                case 500:
                    System.out.println(e.getHttpStatusCode());
                    break;
            }

        } catch (IOException e) {
            //서버 연결 실패
            e.printStackTrace();
        }
    }
}