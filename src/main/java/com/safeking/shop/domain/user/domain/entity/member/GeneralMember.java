package com.safeking.shop.domain.user.domain.entity.member;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@DiscriminatorValue("General")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GeneralMember extends Member{
    /**
     * 일반 회원가입 유저*/
    @Builder
    public GeneralMember(Long id, String username, String password, String email, String roles) {
        super(id, username, password, email, roles);
    }

}
