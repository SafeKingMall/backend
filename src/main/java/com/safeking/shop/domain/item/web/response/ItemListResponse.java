package com.safeking.shop.domain.item.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class ItemListResponse {
    private Long id;
    private Integer price;
    private String name;
    private String categoryName;
    private String createDate;
    private String lastModifiedDate;
}
