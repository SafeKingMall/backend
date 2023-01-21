package com.safeking.shop.domain.payment;

import com.siot.IamportRestClient.IamportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 아임포트 라이브러리 빈 등록
 */
@Configuration
public class IamportConfig {
    private final String apiKey = "8177680216511350";
    private final String apiSecret = "eXxBCpFRI92d8FITgDalK1n5EC4ZTyrQXe4tmuW1GbCPq5IVIFOGRezez0T8Slx9Y293TPYM7BOIhKbT";
    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}
