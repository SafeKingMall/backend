package com.safeking.shop.domain.item.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Range;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemUpdateRequest {

    private Long id;

    private String name;

    private int quantity;

    private String description;

    @Range(min = 100, message = "최소입력 금액은 100원 입니다.")
    private int price;

    private String adminId;

    private Long categoryId;

    private String viewYn="N";


}
