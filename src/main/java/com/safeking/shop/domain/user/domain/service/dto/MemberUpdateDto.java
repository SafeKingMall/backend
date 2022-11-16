package com.safeking.shop.domain.user.domain.service.dto;

import com.safeking.shop.domain.user.domain.entity.Address;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberUpdateDto {
    private Long id;
    private String name;
    private String password;
    private String email;
    private String phoneNumber;
    private Address address;

    @Builder
    public MemberUpdateDto(String name, String password, String email, String phoneNumber, Address address) {

        this.name=name;
        this.password = password;
        this.email = email;
        this.phoneNumber=phoneNumber;
        this.address=address;

    }
}
