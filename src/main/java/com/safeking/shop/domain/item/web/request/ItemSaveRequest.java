package com.safeking.shop.domain.item.web.request;

import com.safeking.shop.domain.item.domain.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

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

    @Range(min = 100, message = "최소입력 금액은 100원 입니다.")
    private int price;

    private Long categoryId;

    private String viewYn="Y";


}
