package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.repository.CacheMemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final CacheMemberRepository cacheMemberRepository;
    private final MemberRepository memberRepository;

    public void cacheRestoration(){
        //1. 캐시를 지우고 2. 캐시를 복구
        cacheMemberRepository.clearStore();
        memberRepository.findAll().stream().forEach(cacheMemberRepository::save);
    }

}
