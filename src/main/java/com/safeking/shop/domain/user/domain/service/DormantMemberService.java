package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.service.dto.AuthenticationInfoDto;
import com.safeking.shop.domain.user.domain.service.dto.CriticalItemsDto;
import com.safeking.shop.domain.user.domain.service.dto.MemberInfoDto;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DormantMemberService {

    private final MemoryDormantRepository dormantRepository;
    private final MemberRepository memberRepository;
    private final CustomBCryPasswordEncoder encoder;

    public Long addCriticalItems(CriticalItemsDto criticalItemsDto){

        Member dormant = memberRepository.findByUsername(criticalItemsDto.getUsername())
                .orElseThrow(() -> new MemberNotFoundException("일치하는 회원이 없습니다."));

        String encodePassword = encoder.encode(criticalItemsDto.getPassword());

        dormant.addCriticalItemsForDormant(encodePassword,criticalItemsDto.getEmail());
        dormant.addLastLoginTime();

        dormantRepository.save(dormant);
        return dormant.getId();
    }
    public Long addAuthenticationInfo(Long id, AuthenticationInfoDto authenticationInfoDto){

        Member dormant = dormantRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        dormant.addAuthenticationInfo(authenticationInfoDto.getName(),authenticationInfoDto.getBirth(),authenticationInfoDto.getPhoneNumber());
        return dormant.getId();
    }

    public Long addMemberInfo (Long id, MemberInfoDto memberInfoDto){
        Member dormant = dormantRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        dormant.addMemberInfo(
                memberInfoDto.getCompanyName()
                ,memberInfoDto.getCompanyRegistrationNumber()
                ,memberInfoDto.getCorporateRegistrationNumber()
                ,memberInfoDto.getRepresentativeName()
                ,memberInfoDto.getAddress()
                ,memberInfoDto.getContact()
        );
        return dormant.getId();
    }
    /**
     * 휴면계정 복구 정보가 다 들어올 시에만 휴면계정 복구
     * member field 의 accountNonLocked 가 false -> true
     **/
    public Long revertCommonAccounts(Long id, Boolean agreement){

        try{
            if(agreement!=true)throw new IllegalArgumentException("약관 동의를 하지 않았습니다.");

            Member dormant = dormantRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

            //휴먼의 개인 정보수집, 휴먼 계정에서 일반 계정으로 변환
            dormant.addAgreement(true);
            dormant.revertCommonAccounts();
            //필요한 게 다 있는지 check하는 로직
            if(!dormant.isCheckedItem())throw new IllegalArgumentException("필수 항목들을 모두 기입해주세요");

            //실제 db에 반영
            Member member = memberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("회원이 존재하지 않습니다."));

            member.updateInfoFromDormant(
                    dormant.getName()
                    ,dormant.getBirth()
                    ,dormant.getPassword()
                    ,dormant.getEmail()
                    ,dormant.getCompanyName()
                    ,dormant.getRepresentativeName()
                    ,dormant.getPhoneNumber()
                    ,dormant.getCompanyRegistrationNumber()
                    ,dormant.getCorporateRegistrationNumber()
                    ,dormant.getAddress()
                    ,dormant.getContact()
                    ,dormant.getAgreement()
                    ,dormant.getAccountNonLocked()
                    ,dormant.getStatus()
            );
            member.addLastLoginTime();
            return member.getId();

        }finally {
            dormantRepository.delete(id);
        }
    }

}
