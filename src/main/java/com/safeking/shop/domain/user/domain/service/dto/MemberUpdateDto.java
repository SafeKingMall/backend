package com.safeking.shop.domain.user.domain.service.dto;

import lombok.Data;

@Data
public class MemberUpdateDto {
    private Long id;
    private String password;
    private String email;

    public MemberUpdateDto(String password, String email) {
        this.password = password;
        this.email = email;
    }
}
