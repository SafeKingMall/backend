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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "member_id")
    private Member member;

    private Long oauthId;

    private String email;

    private String name;

    private OAuthProvider provider;

    @Builder
    public SocialAccount(Long oauthId, String email, String name, OAuthProvider provider) {
        this.oauthId = oauthId;
        this.email = email;
        this.name = name;
        this.provider = provider;

        this.member = new Member(MemberAccountType.SOCIAL);
    }

    public void updateOAuthInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }
}
