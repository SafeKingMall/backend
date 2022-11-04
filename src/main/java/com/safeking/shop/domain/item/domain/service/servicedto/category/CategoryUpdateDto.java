package com.safeking.shop.domain.item.domain.service.servicedto.category;

import lombok.Data;

@Data
public class CategoryUpdateDto {
    private Long id;

    private String name;

    public CategoryUpdateDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
