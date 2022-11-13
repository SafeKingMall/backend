package com.safeking.shop.domain.user.domain.service.dto;

import com.safeking.shop.domain.user.domain.entity.Address;
import lombok.Builder;
import lombok.Data;

@Data
public class GeneralSingUpDto {
    private String name;
    private String username;
    private String password;
    private String email;
    private String phoneNumber;
    private Address address;


    @Builder
    public GeneralSingUpDto(String name, String username, String password, String email, String phoneNumber, Address address) {

        this.name=name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.address=address;

    }
}
