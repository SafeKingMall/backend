package com.safeking.shop.domain.item.web.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAnswerSaveRequest {


    private Long id;

    private String contents;

    private String memberId;

    private Long itemQnaId;


}
