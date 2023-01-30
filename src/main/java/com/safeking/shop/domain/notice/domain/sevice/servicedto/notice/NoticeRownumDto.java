package com.safeking.shop.domain.notice.domain.sevice.servicedto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;


public interface  NoticeRownumDto {
    Long getId();
    String getTitle();
    String getContents();
    String getMemberId();
    String getCreateDate();
    String getLastModifiedDate();

    Integer getRownum();
}
