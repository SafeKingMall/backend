package com.safeking.shop.domain.user.domain.service.dto;

import lombok.Builder;
import lombok.Data;

@Data
public class GeneralSingUpDto {
    private String name;
    private String username;
    private String password;
    private String email;

    @Builder
    public GeneralSingUpDto(String name,String username, String password, String email) {

        this.name=name;
        this.username = username;
        this.password = password;
        this.email = email;

    }
}
