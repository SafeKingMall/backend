package com.safeking.shop.global.security.role;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;

public class RoleFactory {

    public static Role getRoleByMemberStatus(MemberStatus status) {
        switch (status) {
            case TEMP:
                return Role.TEMP_USER;
            case ACTIVATE:
                return Role.ACTIVE_USER;
            default:
                throw new IllegalArgumentException("유효하지 않은 회원의 상태입니다.");
        }
    }
}
