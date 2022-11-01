package com.safeking.shop.domain.user.domain.entity;

public enum MemberStatus {

    ACTIVATE("모든 인증과정을 마친 이용 가능한 유저"),
    TEMP("로그인 정보만을 가진 임시 유저"),
    WITHDRAWN("탈퇴한 유저"),
    ;

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
