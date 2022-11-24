package com.safeking.shop.domain.user.web.response;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class MemberListDto {

    private Long memberId;
    private String name;
    private String memberStatus;

    @QueryProjection
    public MemberListDto(Long memberId, String name, String memberStatus) {
        this.memberId = memberId;
        this.name = name;
        this.memberStatus = memberStatus;
    }
}
