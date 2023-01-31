package com.safeking.shop.domain.item.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAnswerListResponse {


    private Long id;

    private String contents;

    private String memberId;

    private String createDate;

    private String lastModifiedDate;

    private Long itemQnaId;


}
