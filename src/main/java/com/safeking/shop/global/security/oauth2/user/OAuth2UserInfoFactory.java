package com.safeking.shop.global.security.oauth2.user;

import com.safeking.shop.domain.user.domain.entity.OAuthProvider;
import com.safeking.shop.global.security.oauth2.user.impl.GoogleUserInfo;
import com.safeking.shop.global.security.oauth2.user.impl.KakaoUserInfo;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuthUserInfo(String providerName, Map<String, Object> userAttributes) {
        switch (OAuthProvider.from(providerName)) {
            case GOOGLE:
                return new GoogleUserInfo(userAttributes);
            case KAKAO:
                return new KakaoUserInfo(userAttributes);
            default:
                throw new IllegalArgumentException("지원하지 않는 Provider 입니다.");
        }
    }
}
