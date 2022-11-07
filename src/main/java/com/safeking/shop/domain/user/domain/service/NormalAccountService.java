package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import com.safeking.shop.domain.user.domain.repository.NormalAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class NormalAccountService {

    private final NormalAccountRepository normalAccountRepository;

    public Long addNormalAccount(String loginId, String password, String email) {
        // TODO CustomException
        normalAccountRepository.findByLoginIdFetchMember(loginId)
                .ifPresent(normalAccount -> {
                    throw new RuntimeException();
                });

        return normalAccountRepository.save(
                NormalAccount.builder()
                        .loginId(loginId)
                        .pasword(password)
                        .email(email)
                        .build()
        ).getId();
    }
}
