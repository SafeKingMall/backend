package com.safeking.shop.domain.user.web.query.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.QMember;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.safeking.shop.domain.user.domain.entity.member.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Optional<Member> findMemberById(Long id){
        return Optional.ofNullable(
          queryFactory.selectFrom(member)
                  .where(member.id.eq(id))
                  .fetchOne()
        );
    }

}
