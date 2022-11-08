package com.safeking.shop.domain.user.web.request;

import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SignUpRequest {

    private String name;
    private String username;
    private String password;
    private String email;

    public GeneralSingUpDto toServiceDto(){
        return GeneralSingUpDto.builder()
                .name(name)
                .username(username)
                .password(password)
                .email(email)
                .build();
    }
}