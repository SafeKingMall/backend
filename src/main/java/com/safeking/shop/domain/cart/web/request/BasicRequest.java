package com.safeking.shop.domain.cart.web.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BasicRequest {

    @NotNull
    private Long itemId;

    @NotNull
    @Size(max = 50000)
    private int count;

}
