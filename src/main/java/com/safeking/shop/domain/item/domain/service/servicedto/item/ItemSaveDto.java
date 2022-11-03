package com.safeking.shop.domain.item.domain.service.servicedto.item;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategoryDto;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategorySaveDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class ItemSaveDto {

//    private Long id;

    private String name;

    private int quantity;

    private Admin admin;

    private String description;

    private int price;

//    private List <CategoryDto> categories;

    private List<Long> categories;

}
