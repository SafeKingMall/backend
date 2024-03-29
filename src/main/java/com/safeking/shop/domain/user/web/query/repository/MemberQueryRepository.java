package com.safeking.shop.domain.user.web.query.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.EnumPath;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.QMember;
import com.safeking.shop.domain.user.web.response.MemberListDto;
import com.safeking.shop.domain.user.web.response.QMemberListDto;
import com.safeking.shop.domain.user.web.response.QWithDrawlListDto;
import com.safeking.shop.domain.user.web.response.WithDrawlListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.safeking.shop.domain.user.domain.entity.member.QMember.member;
import static org.apache.logging.log4j.util.Strings.isEmpty;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {

    private final JPAQueryFactory queryFactory;

    public Page <MemberListDto> searchAllCondition(String name, String status, Pageable pageable){
        List<MemberListDto> result = queryFactory
                .select(new QMemberListDto(member.id, member.name, member.status))
                .from(member)
                .where(usernameEq(name))
                .where(statusEq(status))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                ;

        JPAQuery<Long> CountQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(usernameEq(name))
                ;

        return PageableExecutionUtils.getPage(result,pageable,CountQuery::fetchOne);
    }

    public Page <WithDrawlListDto> searchWithDrawlList(String name, Pageable pageable){
        List<WithDrawlListDto> result = queryFactory
                .select(new QWithDrawlListDto(member.id, member.name, member.lastLoginTime))
                .from(member)
                .where(member.status.eq(MemberStatus.WITHDRAWAL))
                .where(usernameEq(name))
                .orderBy(member.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch()
                ;

        JPAQuery<Long> CountQuery = queryFactory
                .select(member.count())
                .from(member)
                .where(member.status.eq(MemberStatus.WITHDRAWAL))
                .where(usernameEq(name))
                ;

        return PageableExecutionUtils.getPage(result,pageable,CountQuery::fetchOne);
    }


    private BooleanExpression usernameEq(String name){
        return !isEmpty(name) ? member.name.contains(name) : null;
    }
    private BooleanExpression statusEq(String status){
        return !isEmpty(status) ? member.status.stringValue().contains(status) : null;
    }




}
