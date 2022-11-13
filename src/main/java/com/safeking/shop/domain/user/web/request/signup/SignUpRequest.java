package com.safeking.shop.domain.user.web.request.signup;

import com.safeking.shop.domain.user.domain.entity.Address;
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
    private String phoneNumber;

    private String city;
    private String street;
    private String zipcode;


    public GeneralSingUpDto toServiceDto(){
        return GeneralSingUpDto.builder()
                .name(name)
                .username(username)
                .password(password)
                .email(email)
                .phoneNumber(phoneNumber)
                .address(new Address(city,street,zipcode))
                .build();
    }
}