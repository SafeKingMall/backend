package com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemAnswerUpdateDto {

    private Long id;

    private String contents;
}
