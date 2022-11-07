package com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemAnswerSaveDto {

    private Admin admin;

    private Long itemQuestionId;

    private String contents;
}
