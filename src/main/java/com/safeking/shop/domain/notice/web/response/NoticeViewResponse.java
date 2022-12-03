package com.safeking.shop.domain.notice.web.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class NoticeViewResponse {
    private Long id;
    private String title;
    private String contents;
    private String memberId;
    private String createDate;
    private String lastModifiedDate;
}
