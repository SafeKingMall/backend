package com.safeking.shop.domain.item.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponse {
    private Long id;
    private Integer viewPrice;
    private String name;
    private String categoryName;
    private String createDate;
    private String lastModifiedDate;

    private String fileName;

    private int quantity;
}
