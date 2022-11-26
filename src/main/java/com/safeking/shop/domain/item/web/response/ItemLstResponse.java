package com.safeking.shop.domain.item.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemLstResponse {
    private Long id;
    private String name;
    private String categoryName;
    private String createDate;
    private String lastModifiedDate;
}
