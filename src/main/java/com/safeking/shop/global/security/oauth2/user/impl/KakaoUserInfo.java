package com.safeking.shop.global.security.oauth2.user.impl;

import com.safeking.shop.global.security.oauth2.user.OAuth2UserInfo;
import com.safeking.shop.domain.user.domain.entity.OAuthProvider;

import java.util.Map;

public class KakaoUserInfo extends OAuth2UserInfo {

    private static final OAuthProvider PROVIDER = OAuthProvider.KAKAO;

    public KakaoUserInfo(Map<String, Object> userAttributes) {
        super(userAttributes);
    }

    @Override
    public String getOAuthId() {
        return this.userAttributes.get("id").toString();
    }

    @Override
    public String getName() {
        Map<String, Object> properties = (Map<String, Object>) this.userAttributes.get("properties");
        if (properties == null) {
            return null;
        }
        return (String) properties.get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> kakaoAccount = (Map<String, Object>) this.userAttributes.get("kakao_account");
        if (kakaoAccount == null) {
            return null;
        }
        return  (String) kakaoAccount.get("email");
    }

    @Override
    public OAuthProvider getProvider() {
        return PROVIDER;
    }
}
