package com.safeking.shop.domain.item.web.response;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemViewResponse {
    private Long id;
    private String name;
    private int quantity;
    private String description;
    private Integer viewPrice;
    private String adminId;
    private String categoryName;
    private String createDate;
    private String lastModifiedDate;

    private String fileName;
}
