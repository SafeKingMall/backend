package com.safeking.shop.domain.item.web.response;

import com.safeking.shop.domain.item.domain.entity.ItemAnswer;
import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerViewDto;
import lombok.*;

import java.util.List;

@Getter
@AllArgsConstructor
public class ItemQuestionViewResponse {

    private Long id;

    private String title;

    private String contents;

    private Long itemId;

    private String memberId;//수정 가능, id OR member

    private List<ItemAnswerViewDto> answer;
}
