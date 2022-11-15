package com.safeking.shop.domain.user.web.query.service.dto;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.Data;

@Data
public class MemberInfo {

    private String name;
    private String email;

    public MemberInfo(Member member) {
        this.name=member.getName();
        this.email=member.getEmail();
    }
}
