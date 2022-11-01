package com.safeking.shop.global.security.oauth2.service;

import com.safeking.shop.global.security.role.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collections;
import java.util.Map;

public class UserIdOAuth2User extends DefaultOAuth2User {

    private Long userId;

    public UserIdOAuth2User(Long userId, Role role,
                            Map<String, Object> attributes,
                            String nameAttributeKey) {
        super(Collections.singletonList(new SimpleGrantedAuthority(role.getRoleValue())),
                attributes,
                nameAttributeKey);
        this.userId = userId;
    }

    public boolean requiredAdditionalAuth() {
        // 권한이 TEMP_USER 이면 true
        return this.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .noneMatch(s -> s.equals(Role.TEMP_USER.getRoleValue()));
    }

    public Long getUserId() {
        return userId;
    }
}
