package com.safeking.shop.domain.user.domain.entity.member;

import lombok.*;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Getter
@DiscriminatorValue("Oauth")
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OauthMember extends Member{
    /**
     * 소셜 로그인 회원 가입 유저*/
    private String provider;
    private String providerId;


}
