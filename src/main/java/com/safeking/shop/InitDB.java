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
                    .name("admin")
                    .humanAccount(true)
                    .roles("ROLE_ADMIN").build();
            admin.addLastLoginTime();

            for (int i = 1; i <=30 ; i++) {
                Member user = GeneralMember.builder()
                        .username("user"+i)
                        .password(encoder.encode("1234"))
                        .name("user"+i)
                        .humanAccount(true)
                        .roles("ROLE_USER").build();
                user.addLastLoginTime();
                em.persist(user);

            }
            Member user = GeneralMember.builder()
                    .username("human")
                    .password(encoder.encode("1234"))
                    .name("human")
                    .humanAccount(false)
                    .roles("ROLE_USER").build();
            user.addLastLoginTime();
            em.persist(user);



            em.persist(admin);
        }
    }

}