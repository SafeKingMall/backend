package com.safeking.shop.domain.user.domain.service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
public class CriticalItemsDto {

    private String username;
    private String password;
    private String email;

}
