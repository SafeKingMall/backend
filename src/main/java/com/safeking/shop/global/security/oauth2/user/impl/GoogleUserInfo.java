package com.safeking.shop.global.security.oauth2.user.impl;

import com.safeking.shop.global.security.oauth2.user.OAuth2UserInfo;
import com.safeking.shop.domain.user.domain.entity.OAuthProvider;

import java.util.Map;

// TODO 데이터 확인 후 구현
public class GoogleUserInfo extends OAuth2UserInfo {

    public GoogleUserInfo(Map<String, Object> userAttributes) {
        super(userAttributes);
    }

    @Override
    public Long getOAuthId() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getEmail() {
        return null;
    }

    @Override
    public OAuthProvider getProvider() {
        return null;
    }
}
