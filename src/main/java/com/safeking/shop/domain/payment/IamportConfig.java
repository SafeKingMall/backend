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
    private final String apiSecret = "zpbnXVhACL6hAQK6LYEohYTTQBGT7fF8BaWeH1WhOKZUhTrp5Ws4m6YZR6GGyxJAGbJZr1zHshI9aiJx";
    @Bean
    public IamportClient iamportClient() {
        return new IamportClient(apiKey, apiSecret);
    }
}
