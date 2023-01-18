package com.safeking.shop.domain.item.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ItemAdminViewResponse {
    private Long id;
    private String name;
    private int quantity;
    private String description;
    private Integer price;
    private String adminId;
    private String categoryName;
    private String createDate;
    private String lastModifiedDate;

    private String fileName;
}
