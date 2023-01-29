package com.safeking.shop.domain.item.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAnswerUpdateRequest {

    private Long id;
    private String contents;

}
