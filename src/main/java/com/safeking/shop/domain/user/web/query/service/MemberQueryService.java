package com.safeking.shop.domain.user.web.query.service;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.web.response.MemberDetails;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberQueryService {

    private final MemberRepository memberRepository;

    public MemberDetails showMemberDetails(String username){
        return memberRepository.findByUsername(username)
                .map(MemberDetails::new)
                .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));
    }


}
