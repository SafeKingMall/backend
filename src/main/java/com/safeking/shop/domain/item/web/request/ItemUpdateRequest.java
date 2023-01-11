package com.safeking.shop.domain.item.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    private Long id;

    private String name;

    private int quantity;

    private String description;

    private int price;

    private String adminId;

    private Long categoryId;

    private String viewYn="N";


}
