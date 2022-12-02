package com.safeking.shop.domain.item.web.response;

import com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer.ItemAnswerViewDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class ItemQuestionListResponse {

    private Long id;

    private String title;

    private Long itemId;

    private String memberId;

    private String createDate;

    private String lastModifiedDate;
}
