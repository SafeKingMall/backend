package com.safeking.shop.domain.item.domain.service.servicedto.category;

import lombok.Data;

@Data
public class CategorySaveDto {

    private String name;


    public CategorySaveDto(String name) {
        this.name = name;
    }
}
