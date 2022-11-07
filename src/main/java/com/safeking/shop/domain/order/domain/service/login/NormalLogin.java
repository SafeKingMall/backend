package com.safeking.shop.domain.order.domain.service.login;

import com.safeking.shop.domain.exception.LoginException;
import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import com.safeking.shop.domain.user.domain.repository.NormalAccountRepository;

import java.util.Optional;

public class NormalLogin implements LoginBehavior {
    private final NormalAccountRepository normalAccountRepository;
    private String loginId;

    public NormalLogin(NormalAccountRepository normalAccountRepository, String loginId) {
        this.normalAccountRepository = normalAccountRepository;
        this.loginId = loginId;
    }

    @Override
    public NormalAccount login() {
        Optional<NormalAccount> findMember = normalAccountRepository.findByLoginIdFetchMember(loginId);
        return findMember.orElseThrow(() -> new LoginException("회원이 없습니다."));
    }
}
