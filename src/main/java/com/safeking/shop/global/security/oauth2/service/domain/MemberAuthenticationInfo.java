package com.safeking.shop.global.security.oauth2.service.domain;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import lombok.Getter;

@Getter
public class MemberAuthenticationInfo {

    private Long memberId;
    private MemberStatus status;

    public MemberAuthenticationInfo(Long memberId, MemberStatus status) {
        this.memberId = memberId;
        this.status = status;
    }
}
