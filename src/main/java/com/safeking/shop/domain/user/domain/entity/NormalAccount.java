package com.safeking.shop.domain.user.domain.entity;

import com.safeking.shop.domain.admin.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NormalAccount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "normal_account_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    private String loginId;

    private String pasword;

    private String email;

    @Builder
    public NormalAccount(String loginId, String pasword, String email) {
        this.loginId = loginId;
        this.pasword = pasword;
        this.email = email;

        this.member = new Member(MemberAccountType.NORMAL);
    }
}
