package com.safeking.shop.domain.item.domain.service.servicedto.ItemQuestion;

import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerViewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemQuestionListDto {

    private Long id;

    private String title;

    private Long itemId;

    private String memberId;

    private String createDate;

    private String lastModifiedDate;

}
