package com.safeking.shop.domain.notice.domain.sevice.servicedto.notice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NoticeSaveDto {

    private String title;
    private String contents;
    private String memberId;

}
