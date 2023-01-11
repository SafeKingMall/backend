package com.safeking.shop.domain.item.web.request;

import com.safeking.shop.domain.item.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemSaveRequest {

    //private Long id;

    private String name;

    private int quantity;

    private String adminId;

    private String description;

    private int price;

    private Long categoryId;

    private String viewYn="Y";


}
