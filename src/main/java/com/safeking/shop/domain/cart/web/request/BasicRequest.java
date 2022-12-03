package com.safeking.shop.domain.cart.web.request;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BasicRequest {

    @NotNull
    private Long itemId;
    @NotNull
    private int count;

}
