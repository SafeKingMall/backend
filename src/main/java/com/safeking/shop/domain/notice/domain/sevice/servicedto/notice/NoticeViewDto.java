package com.safeking.shop.domain.notice.domain.sevice.servicedto.notice;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoticeViewDto {
    private Long id;
    private String title;
    private String contents;
    private String memberId;
    private String createDate;
    private String lastModifiedDate;
}
