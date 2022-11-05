package com.safeking.shop.domain.order.domain.service.login;

import com.safeking.shop.domain.order.domain.exception.LoginException;
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
    public LoginDto login() {
        Optional<NormalAccount> findMember = normalAccountRepository.findByLoginIdFetchMember(loginId);
        Optional<LoginDto> loginDto = findMember.map(m -> new LoginDto(m.getMember().getId()));

        return loginDto.orElseThrow(() -> new LoginException("해당 회원이 없습니다."));
    }
}
