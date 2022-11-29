package com.safeking.shop;

import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

@Component
@RequiredArgsConstructor
public class InitDB {

    private final InitService initService;

    @PostConstruct
    public void init(){
        initService.init1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        private final CustomBCryPasswordEncoder encoder;

        public void init1(){
            Member admin = GeneralMember.builder()
                    .username("admin")
                    .password(encoder.encode("1234"))
                    .name("admin")
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .roles("ROLE_ADMIN").build();
            admin.addLastLoginTime();

            for (int i = 1; i <=30 ; i++) {
                Member user = GeneralMember.builder()
                        .username("user"+i)
                        .password(encoder.encode("1234"))
                        .name("user"+i)
                        .accountNonLocked(true)
                        .status(MemberStatus.COMMON)
                        .roles("ROLE_USER").build();
                user.addLastLoginTime();
                em.persist(user);

            }
            Member user = GeneralMember.builder()
                    .username("human")
                    .password(encoder.encode("1234"))
                    .name("human")
                    .accountNonLocked(false)
                    .status(MemberStatus.HUMAN)
                    .roles("ROLE_USER").build();
            user.addLastLoginTime();

            Member minsung = GeneralMember.builder()
                    .name("minsung")
                    .birth("971202")
                    .username("kms199711")
                    .password(encoder.encode("kms92460771*"))
                    .email("kms199719@naver.com")
                    .roles("ROLE_USER")
                    .phoneNumber("01082460887")
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .companyName("safeking")
                     .build();
            minsung.addLastLoginTime();


            em.persist(user);
            em.persist(admin);
            em.persist(minsung);
        }
    }

}