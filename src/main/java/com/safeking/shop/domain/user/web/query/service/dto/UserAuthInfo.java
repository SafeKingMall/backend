package com.safeking.shop.domain.user.web.query.service.dto;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserAuthInfo {

    private Long userId;
    private MemberStatus status;
}
