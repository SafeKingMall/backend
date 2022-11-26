package com.safeking.shop.domain.item.web.request;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemRequest {
    private String name;
    private int quantity;
    private String description;
    private int price;
    private Admin admin;
    private List<Long> categories;
}
