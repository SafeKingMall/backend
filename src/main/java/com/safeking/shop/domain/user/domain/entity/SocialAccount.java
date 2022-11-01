package com.safeking.shop.domain.user.domain.entity;

import com.safeking.shop.domain.common.BaseTimeEntity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SocialAccount extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "social_account_id")
    private Long id;

    private Long oauthId;

    private String email;

    private String name;

    private OAuthProvider provider;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Builder
    public SocialAccount(Long oauthId, String email, String name, OAuthProvider provider, Member member) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.provider = provider;
        this.member = member;
    }
}
