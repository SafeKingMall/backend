package com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemAnswerSaveDto {

    private Member member;

    private Long itemQuestionId;

    private String contents;
}
