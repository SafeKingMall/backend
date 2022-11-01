package com.safeking.shop.global.security.oauth2.user.impl;

import com.safeking.shop.global.security.oauth2.user.OAuth2UserInfo;
import com.safeking.shop.domain.user.domain.entity.OAuthProvider;

import java.util.Map;

public class GoogleUserInfo extends OAuth2UserInfo {

    private static final OAuthProvider PROVIDER = OAuthProvider.GOOGLE;

    public GoogleUserInfo(Map<String, Object> userAttributes) {
        super(userAttributes);
    }

    @Override
    public String getOAuthId() {
        return this.userAttributes.get("sub").toString();
    }

    @Override
    public String getName() {
        return this.userAttributes.get("name").toString();
    }

    @Override
    public String getEmail() {
        return this.userAttributes.get("email").toString();
    }

    @Override
    public OAuthProvider getProvider() {
        return PROVIDER;
    }
}
