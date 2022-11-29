package com.safeking.shop.domain.item.domain.service.servicedto.item;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemUpdateDto {

    private Long id;

    private String name;

    private int quantity;

    private String description;

    private int price;

    private List<Long> categories;


}
