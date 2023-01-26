package com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemQuestionSaveDto {

    private String title;

    private String contents;

    private Long itemId;

    private String memberId;//수정 가능, id OR member

    private String password;

}
