package com.safeking.shop.domain.notice.web.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class NoticeListResponse {
    private Long id;
    private String title;
    private String memberId;
    private String createDate;
    private String lastModifiedDate;
}
