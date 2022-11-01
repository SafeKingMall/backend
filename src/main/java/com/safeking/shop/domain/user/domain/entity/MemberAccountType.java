package com.safeking.shop.domain.user.domain.entity;

public enum MemberAccountType {

    NORMAL("일반 로그인 계정"),
    SOCIAL("소셜 로그인 계정"),
    ;

    private final String description;

    MemberAccountType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
