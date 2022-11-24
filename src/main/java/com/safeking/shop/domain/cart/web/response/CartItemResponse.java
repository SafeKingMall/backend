package com.safeking.shop.domain.cart.web.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CartItemResponse {

    private String itemName;
    private int itemPrice;
    private int itemQuantity;

    @QueryProjection
    public CartItemResponse(String itemName, int itemPrice, int itemQuantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
    }
}
