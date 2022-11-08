package com.safeking.shop.domain.user.domain.entity.member;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("Oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthMember extends Member{
    /**
     * 소셜 로그인 회원 가입 유저*/
    private String provider;
    private String providerId;

    @Builder
    public OauthMember(Long id, String name, String username, String password, String email, String roles,String provider, String providerId) {

        super(id, name,username, password, email, roles);

        this.provider = provider;
        this.providerId = providerId;

    }
}
