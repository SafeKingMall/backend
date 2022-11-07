package com.safeking.shop.domain.user.domain.entity.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Setter
@DiscriminatorValue("Oauth")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthMember extends Member{

    private String provider;
    private String providerId;

    @Builder
    public OauthMember(Long id, String username, String password, String email, String roles,String provider, String providerId) {

        super(id,username,password,email,roles);

        this.provider = provider;
        this.providerId = providerId;

    }
}
