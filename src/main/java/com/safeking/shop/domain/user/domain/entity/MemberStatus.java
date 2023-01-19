package com.safeking.shop.domain.user.domain.entity;

import lombok.Getter;


public enum MemberStatus {
    COMMON("일반 회원"), HUMAN("휴먼 회원"), WITHDRAWAL("탈퇴 회원");

    private final String description;

    MemberStatus(String description) {
        this.description = description;
    }

    public static String getDescription(String status) {

        if (status == "COMMON") return COMMON.description;
        else if (status == "HUMAN") return HUMAN.description;
        else return WITHDRAWAL.description;

    }

}
