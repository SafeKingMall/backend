package com.safeking.shop.domain.coolsms.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Data
@Component
public class ApiConfigVo {
    private final Environment env;
    private final String key;

    private final String password;

    public ApiConfigVo(Environment env) {
        this.env = env;
        this.key = env.getProperty("CoolSms.API_KEY");
        this.password = env.getProperty("CoolSms.API_SECRET");
    }
}
