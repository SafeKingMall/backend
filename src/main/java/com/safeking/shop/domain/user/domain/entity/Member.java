package com.safeking.shop.domain.user.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberAccountType type;

    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    public Member(MemberAccountType type) {
        this.type = type;
        this.status = MemberStatus.TEMP;
    }

    public void updateActivateMember() {
        this.status = MemberStatus.ACTIVATE;
    }
}
