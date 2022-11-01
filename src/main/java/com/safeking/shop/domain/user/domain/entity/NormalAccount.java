package com.safeking.shop.domain.user.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NormalAccount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "normal_account_id")
    private Long id;

    private String loginId;

    private String pasword;

    private String email;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    public NormalAccount(String loginId, String pasword, String email, Member member) {
        this.loginId = loginId;
        this.pasword = pasword;
        this.email = email;
        this.member = member;
    }
}
