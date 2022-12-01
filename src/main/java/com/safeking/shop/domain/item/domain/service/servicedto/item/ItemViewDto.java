package com.safeking.shop.domain.item.domain.service.servicedto.item;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemViewDto {

    private Long id;
    private String name;
    private int quantity;
    private String description;
    private int price;
    private String adminId;
    private List<Long> categories;
    private String categoryName;
    private String createDate;
    private String lastModifiedDate;
    private String viewYn;

}
