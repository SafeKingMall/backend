package com.safeking.shop.domain.item.domain.service.servicedto.item;

import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.entity.CategoryItem;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
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
