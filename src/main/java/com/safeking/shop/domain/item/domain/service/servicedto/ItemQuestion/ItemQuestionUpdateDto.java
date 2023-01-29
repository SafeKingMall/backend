package com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemQuestionUpdateDto {

    private Long id;

    private String title;

    private String contents;

    private String password;


}
