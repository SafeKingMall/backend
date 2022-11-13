package com.safeking.shop.domain.coolsms.request;

import lombok.Data;

@Data
public class SMSRequest {

    private String clientPhoneNumber;

    private String code;

}
