package com.safeking.shop.domain.item.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CategoryListResponse {
    private Long id;
    private String name;
    private String createDate;
    private String lastModifiedDate;
}
