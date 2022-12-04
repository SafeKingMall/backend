package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CacheService {

    private final MemberRedisRepository cacheMemberRepository;
    private final MemberRepository memberRepository;



}
