package com.safeking.shop.global.security.oauth2.user;

import com.safeking.shop.domain.user.domain.entity.OAuthProvider;

import java.util.Map;

public abstract class OAuth2UserInfo {

    // Provider 에게서 제공받은 유저 정보를 담는 필드
    protected Map<String, Object> userAttributes;

    public OAuth2UserInfo(Map<String, Object> userAttributes) {
        this.userAttributes = userAttributes;
    }

    public Map<String, Object> getUserAttributes() {
        return userAttributes;
    }

    public abstract Long getOAuthId();

    public abstract String getName();

    public abstract String getEmail();

    public abstract OAuthProvider getProvider();
}
