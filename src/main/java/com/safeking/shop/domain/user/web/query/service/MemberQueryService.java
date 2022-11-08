package com.safeking.shop.domain.user.web.query.service;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.web.query.repository.MemberQueryRepository;
import com.safeking.shop.domain.user.web.query.service.dto.MemberInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberQueryService {

    private final MemberQueryRepository memberQueryRepository;

    public MemberInfo showMyPage(Long id){
        return memberQueryRepository
                .findMemberById(id).map(MemberInfo::new)
                .orElseThrow();
    }


}
