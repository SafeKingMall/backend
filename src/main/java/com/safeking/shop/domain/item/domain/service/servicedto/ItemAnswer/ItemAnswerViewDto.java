package com.safeking.shop.domain.item.domain.service.servicedto.ItemAnswer;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ItemAnswerViewDto {


    private Long id;

    private String contents;

    private String memberId;

    private String createDate;

    private String lastModifiedDate;

    public ItemAnswerViewDto(Long id, String contents, String memberId, String createDate, String lastModifiedDate){
        this.setId(id);
        this.setContents(contents);
        this.setMemberId(memberId);
        this.setCreateDate(createDate);
        this.setLastModifiedDate(lastModifiedDate);
    }

}
