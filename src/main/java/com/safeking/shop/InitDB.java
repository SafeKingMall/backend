package com.safeking.shop;

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
                    .roles("ROLE_ADMIN").build();

            Member user = GeneralMember.builder()
                    .username("user")
                    .password(encoder.encode("1234"))
                    .roles("ROLE_USER").build();

            em.persist(admin);
            em.persist(user);
        }
    }

}