package com.safeking.shop.domain.user.web.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.user.domain.entity.NormalAccount;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.safeking.shop.domain.user.domain.entity.QNormalAccount.normalAccount;

@Repository
@RequiredArgsConstructor
public class NormalAccountQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<NormalAccount> findByLoginIdFetchMember(String loginId) {
        return Optional.ofNullable(
                queryFactory.selectFrom(normalAccount)
                        .leftJoin(normalAccount.member).fetchJoin()
                        .where(normalAccount.loginId.eq(loginId))
                        .fetchOne()
        );
    }
}
