package com.safeking.shop.domain.cart.web.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

@Data
public class CartItemResponse {

    private Long id;
    private String itemName;
    private int itemPrice;
    private int itemQuantity;
    private String categoryName;
    private String thumbNail;
//    private String categoryName;
    /**
     * @QueryProjection: 한방 쿼리
     **/
    @QueryProjection
    public CartItemResponse(
            Long id
            , String itemName
            , int itemPrice
            , int itemQuantity
            , String categoryName
            , String thumbNail
    ) {
        this.id = id;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemQuantity = itemQuantity;
        this.categoryName=categoryName;
        this.thumbNail =thumbNail;
    }
}
