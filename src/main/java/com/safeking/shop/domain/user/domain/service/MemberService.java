package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final CustomBCryPasswordEncoder encoder;

    public Long join(GeneralSingUpDto singUpDto){
        log.info("회원 가입");

        GeneralMember generalMember = GeneralMember.builder()
                .username(singUpDto.getUsername())
                .password(encoder.encode(singUpDto.getPassword()))
                .email(singUpDto.getEmail())
                .roles("ROLE_USER")
                .build();

        memberRepository.save(generalMember);
        return generalMember.getId();
    }

    public void updateMemberInfo(Long id,MemberUpdateDto memberUpdateDto){
        log.info("회원 정보 수정");

        Member member = memberRepository.findById(id).orElseThrow();

        member.updateMemberInfo(memberUpdateDto.getPassword(), memberUpdateDto.getEmail());
    }

    public void delete(Long id){
        log.info("delete");

        // 추후 만들 예정
    }

}
