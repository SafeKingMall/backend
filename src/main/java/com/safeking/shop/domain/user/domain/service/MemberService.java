package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.coolsms.web.query.SMSService;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.*;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.exception.MemberNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemoryMemberRepository memoryMemberRepository;
    private final CustomBCryPasswordEncoder encoder;

    private final SMSService smsService;

    public Long addCriticalItems(CriticalItemsDto criticalItemsDto){

        GeneralMember generalMember = GeneralMember.builder()
                .username(criticalItemsDto.getUsername())
                .password(encoder.encode(criticalItemsDto.getPassword()))
                .email(criticalItemsDto.getEmail())
                .roles("ROLE_USER")
                .build();

        memoryMemberRepository.save(generalMember);

        return generalMember.getId();
    }

    public Long addAuthenticationInfo(Long id,AuthenticationInfoDto authenticationInfoDto){

        Member member = memoryMemberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        member.addAuthenticationInfo(authenticationInfoDto.getName(),authenticationInfoDto.getBirth(),authenticationInfoDto.getPhoneNumber());

        return member.getId();
    }

    public Long changeMemoryToDB(Long id, Boolean agreement){

        if(agreement!=true)throw new IllegalArgumentException("약관 동의를 하지 않았습니다.");

        Member member = memoryMemberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        //필요한 게 다 있는지 check하는 로직 추가

        member.addAgreement(true);
        member.changeId(null);
        memberRepository.save(member);

        memoryMemberRepository.delete(id);

        return member.getId();
    }


    public Long addMemberInfo(Long id, MemberInfoDto memberInfoDto){
        Member member = memoryMemberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        member.addMemberInfo(memberInfoDto.getCompanyName(),memberInfoDto.getCompanyRegistrationNumber(),memberInfoDto.getCorporateRegistrationNumber(),memberInfoDto.getRepresentativeName(),memberInfoDto.getAddress(),memberInfoDto.getContact());

        return member.getId();
    }

    public boolean idDuplicationCheck(String username){
        //id를 사용가능하다면  true
        return memberRepository.findByUsername(username).orElse(null) == null;
    }

    public void updateMemberInfo(String username,MemberUpdateDto memberUpdateDto){
        log.info("회원 정보 수정");

        memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberNotFoundException("member not found"))
                .updateInfo(memberUpdateDto.getName(),memberUpdateDto.getBirth(),memberUpdateDto.getRepresentativeName(),memberUpdateDto.getPhoneNumber()
                ,memberUpdateDto.getCompanyRegistrationNumber(),memberUpdateDto.getCorporateRegistrationNumber(),memberUpdateDto.getAddress());
    }

    public void updatePassword(String username,String password){
        memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberNotFoundException("회원을 찾을 수가 없습니다."))
                .updatePassword(encoder.encode(password));
    }

    public Long getIdFromUsername(String username){
        return memberRepository.findByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("회원을 찾을 수가 없습니다."))
                .getId();
    }

    public void delete(Long id){
        log.info("delete");

        // 추후 만들 예정
    }

    public void sendTemporaryPassword(String username) throws CoolsmsException {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("아이디와 일치하는 회원이 없습니다."));

        String temporaryPassword = createCode();

        member.changePassword(encoder.encode(temporaryPassword));

        smsService.sendPasswordToClient(member.getPhoneNumber(),temporaryPassword);
    }

    private static String createCode() {
        Random rand  = new Random();
        String code = "";
        for(int i=0; i<7; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            code+=ran;
        }
        return code;
    }

}
