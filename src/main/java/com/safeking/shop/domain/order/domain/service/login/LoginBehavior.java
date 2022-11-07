package com.safeking.shop.domain.order.domain.service.login;

import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import com.safeking.shop.domain.user.domain.entity.SocialAccount;

public interface LoginBehavior {
    Object login();
    default boolean isSocial(Object object) {
        return object instanceof SocialAccount;
    }

    default boolean isNormal(Object object) {
        return object instanceof NormalAccount;
    }
}
