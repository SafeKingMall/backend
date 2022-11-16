package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.coolsms.web.query.SMSService;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.domain.service.dto.MemberUpdateDto;
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
    private final CustomBCryPasswordEncoder encoder;

    private final SMSService smsService;

    public Long join(GeneralSingUpDto singUpDto){
        log.info("회원 가입");

        GeneralMember generalMember = GeneralMember.builder()
                .name(singUpDto.getName())
                .username(singUpDto.getUsername())
                .password(encoder.encode(singUpDto.getPassword()))
                .email(singUpDto.getEmail())
                .phoneNumber(singUpDto.getPhoneNumber())
                .address(singUpDto.getAddress())
                .roles("ROLE_USER")
                .build();

        memberRepository.save(generalMember);
        return generalMember.getId();
    }

    public boolean idDuplicationCheck(String username){
        //id를 사용가능하다면  true
        return memberRepository.findByUsername(username).orElse(null) == null;
    }

    public void updateMemberInfo(Long id,MemberUpdateDto memberUpdateDto){
        log.info("회원 정보 수정");

        Member member = memberRepository.findById(id).orElseThrow(()->new MemberNotFoundException("member not found"));

        member.updateMemberInfo(member.getName(), memberUpdateDto.getPassword(), memberUpdateDto.getEmail(), memberUpdateDto.getPhoneNumber(),memberUpdateDto.getAddress());
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
