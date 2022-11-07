package com.safeking.shop.domain.user.domain.entity.provider;

import java.util.Map;

public class FaceBookUserInfo implements Oauth2UserInfo{
    private Map<String,Object>attributes;

    public FaceBookUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
