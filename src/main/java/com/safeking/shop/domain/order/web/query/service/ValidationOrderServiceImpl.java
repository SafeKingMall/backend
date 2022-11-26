package com.safeking.shop.domain.order.web.query.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.order.web.OrderConst;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.jwt.TokenUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.safeking.shop.global.jwt.TokenUtils.BEARER;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ValidationOrderServiceImpl implements ValidationOrderService {

    private final MemberRepository memberRepository;

    /**
     * 토큰 검증, 회원 검증
     */
    public Member validationMember(String token) {

        String username = TokenUtils.verify(token.replace(BEARER, ""));

        Optional<Member> findMemberOptional = memberRepository.findByUsername(username);
        Member findMember = findMemberOptional.orElseThrow(() -> new OrderException(OrderConst.ORDER_MEMBER_NONE));

        return findMember;
    }
}
