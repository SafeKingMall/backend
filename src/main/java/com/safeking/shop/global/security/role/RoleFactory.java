package com.safeking.shop.global.security.role;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;

public class RoleFactory {

    public static Role getRoleByMemberStatus(MemberStatus status) {
        switch (status) {
            case TEMP:
                return Role.TEMP;
            case ACTIVATE:
                return Role.USER;
            default:
                throw new IllegalArgumentException("유효하지 않은 회원의 상태입니다.");
        }
    }
}
