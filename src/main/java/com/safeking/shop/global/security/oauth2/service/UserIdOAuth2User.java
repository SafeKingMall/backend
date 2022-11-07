package com.safeking.shop.global.security.oauth2.service;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.global.security.role.Role;
import com.safeking.shop.global.security.role.RoleFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;

public class UserIdOAuth2User extends DefaultOAuth2User {

    private Long userId;
    private MemberStatus status;

    public UserIdOAuth2User(Long userId, MemberStatus status,
                            Map<String, Object> attributes,
                            String nameAttributeKey) {
        super(Collections.singletonList(new SimpleGrantedAuthority(RoleFactory.getRoleByMemberStatus(status).getRoleValue())),
                attributes,
                nameAttributeKey);
        this.userId = userId;
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public MemberStatus getStatus() {
        return status;
    }
}
