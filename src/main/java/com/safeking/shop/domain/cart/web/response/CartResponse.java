package com.safeking.shop.domain.cart.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CartResponse {

    private List<CartItemResponse> cartItemResponses;
    private int totalPrice;
    private int deliveryFee;

}
