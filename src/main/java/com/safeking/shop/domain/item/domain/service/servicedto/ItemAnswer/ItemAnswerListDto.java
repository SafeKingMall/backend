package com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer;

import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemAnswerListDto {


    private Long id;

    private String contents;

    private String memberId;

    private String createDate;

    private String lastModifiedDate;

    private Long itemQnaId;


}
