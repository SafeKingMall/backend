package com.safeking.shop.domain.item.domain.service.servicedto.item;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.item.domain.entity.Category;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategoryDto;
import com.safeking.shop.domain.item.domain.service.servicedto.category.CategorySaveDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSaveDto {

    //private Long id;

    private String name;

    private int quantity;

    private String adminId;

    private String description;

    private int price;

    private Category category;

}
