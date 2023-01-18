package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;

    @Test
    void findByStatus() {
        //given
        for (int i = 0; i <30 ; i++) {
            GeneralMember common = GeneralMember.builder()
                    .status(MemberStatus.COMMON)
                    .build();
            memberRepository.save(common);
        }
        for (int i = 0; i <30 ; i++) {
            GeneralMember common = GeneralMember.builder()
                    .status(MemberStatus.WITHDRAWAL)
                    .build();
            memberRepository.save(common);
        }
        //when
        List<Member> withDrawalList = memberRepository.findByStatus(MemberStatus.WITHDRAWAL);
        for (Member member : withDrawalList) {
            System.out.println("member.getStatus() = " + member.getStatus());
        }
        //then
        assertThat(withDrawalList.size()).isEqualTo(30);
        
        
    }
}