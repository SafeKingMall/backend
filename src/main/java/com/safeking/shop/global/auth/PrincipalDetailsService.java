package com.safeking.shop.global.auth;

import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PrincipalDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository;
    /**
     * 1. UsernamePasswordAuthenticationFilter 에서
     * 2. loadUserByUsername 을 실행
     * 3. username 바탕으로 검증
     * 4. member 의 로그인 시간을 update
     * 5. UserDetails 로 감싼다.
     **/
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername 실행");

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberNotFoundException("member not found"));

        member.addLastLoginTime();
        return new PrincipalDetails(member);
    }
}