package com.safeking.shop.domain.user.web.response;

import com.querydsl.core.annotations.QueryProjection;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class WithDrawlListDto {

    private Long memberId;
    private String name;
    private String withdrawalDate;

    @QueryProjection
    public WithDrawlListDto(Long memberId, String name, LocalDateTime lastLoginDate) {
        this.memberId = memberId;
        this.name = name;
        // 시간을 변환
        this.withdrawalDate=lastLoginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
