package com.safeking.shop.domain.user.web.query.service;

import com.safeking.shop.domain.user.domain.entity.Member;
import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import com.safeking.shop.domain.user.web.query.repository.NormalAccountQueryRepository;
import com.safeking.shop.domain.user.web.query.service.dto.UserAuthInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NormalAccountQueryService {

    private final NormalAccountQueryRepository normalAccountQueryRepository;

    public UserAuthInfo getUserAuthInfoByLoginIdAndPassword(String loginId, String password) {
        // TODO CustomException
        NormalAccount normalAccount = normalAccountQueryRepository.findByLoginIdFetchMember(loginId)
                .orElseThrow();
        checkPassword(password, normalAccount);

        Member member = normalAccount.getMember();
        return new UserAuthInfo(member.getId(), member.getStatus());
    }

    private void checkPassword(String password, NormalAccount normalAccount) {
        if (!normalAccount.getPasword().equals(password)) {
            // TODO CustomException
            throw new RuntimeException();
        }
    }
}
