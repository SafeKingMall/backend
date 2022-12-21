package com.safeking.shop.domain.payment.web.client;

import com.safeking.shop.domain.payment.web.Iamport;
import org.springframework.stereotype.Component;

@Component
public class IamportClient {
    public static final String API_URL = "https://api.iamport.kr";
    public static final String STATIC_API_URL = "https://static-api.iamport.kr";
    protected String apiKey = null;
    protected String apiSecret = null;
    protected String tierCode = null;
    protected Iamport iamport = null;
}
