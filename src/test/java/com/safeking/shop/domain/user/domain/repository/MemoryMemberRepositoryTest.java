package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class MemoryMemberRepositoryTest {
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    MemoryMemberRepository memoryMemberRepository;
    @Test
    void findDuplication() {
        //given
        Member user = GeneralMember.builder()
                .username("testUser")
                .build();
        memoryMemberRepository.save(user);
        //when
        boolean duplication = memoryMemberRepository.findDuplication(user.getUsername());
        boolean notDuplication = memoryMemberRepository.findDuplication("no_duplication");
        //then
        assertAll
        (
                ()->assertThat(duplication).isEqualTo(false),
                ()->assertThat(notDuplication).isEqualTo(true)
        );
    }
}