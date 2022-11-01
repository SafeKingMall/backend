package com.safeking.shop.domain.user.domain.entity;

import java.util.stream.Stream;

public enum OAuthProvider {
    GOOGLE("구글"),
    KAKAO("카카오"),
    ;

    private final String description;

    OAuthProvider(String description) {
        this.description = description;
    }

    public static OAuthProvider from(String value) {
        return Stream.of(values())
                .filter(provider -> provider.name().equals(value.toUpperCase()))
                .findFirst()
                .orElse(null);
    }

    public String getDescription() {
        return description;
    }
}
