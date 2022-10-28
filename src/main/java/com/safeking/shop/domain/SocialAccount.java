package com.safeking.shop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
//@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
public class SocialAccount {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "socialaccount_id", nullable = false)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private Long oauthId;
    private String email;
    private String name;
    private String phone;
    private String provider;


}
