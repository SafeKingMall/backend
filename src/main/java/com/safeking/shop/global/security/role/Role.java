package com.safeking.shop.global.security.role;

public enum Role {

    ACTIVE_USER("ROLE_USER", "모든 유저 기능 이용 가능한 유저 권한"),
    TEMP_USER("ROLE_TEMP", "인증 정보 입력만 가능한 임시 유저 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한"),
    ;

    private final String roleValue;
    private final String description;

    Role(String value, String description) {
        this.roleValue = value;
        this.description = description;
    }

    public String getRoleValue() {
        return roleValue;
    }

    public String getDescription() {
        return description;
    }
}
