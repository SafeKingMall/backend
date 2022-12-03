package com.safeking.shop;

import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.CacheMemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
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
        initService.initAdminTestV1();
        initService.initMemberTestV1();
        initService.initItemTestV1();
        initService.initCacheDB();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService{
        private final EntityManager em;
        private final CustomBCryPasswordEncoder encoder;
        private final MemberRepository memberRepository;
        private final CacheMemberRepository cacheMemberRepository;
        private final CartService cartService;

        public void initAdminTestV1(){
            Member admin = GeneralMember.builder()
                    .name("admin")
                    .birth("971202")
                    .username("admin1234")
                    .password(encoder.encode("admin1234*"))
                    .email("kms199719@naver.com")
                    .roles("ROLE_ADMIN")
                    .phoneNumber("01082460887")
                    .companyName("safeking")
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .build();
            admin.addLastLoginTime();

            em.persist(admin);
        }

        public void initMemberTestV1(){
            //일반 회원 30명 넣기
            for (int i = 1; i <=30 ; i++) {
                Member user = GeneralMember.builder()
                        .name("admin")
                        .birth("971202")
                        .username("testUser"+i)
                        .password(encoder.encode("testUser"+i+"*"))
                        .email("kms199719@naver.com")
                        .roles("ROLE_ADMIN")
                        .phoneNumber("01082460887")
                        .companyName("safeking")
                        .companyRegistrationNumber("111")
                        .corporateRegistrationNumber("222")
                        .representativeName("MS")
                        .address(new Address("서울시","마포대로","111"))
                        .agreement(true)
                        .accountNonLocked(true)
                        .status(MemberStatus.COMMON)
                        .build();
                user.addLastLoginTime();
                em.persist(user);
                cartService.createCart(user);

            }
            Member user = GeneralMember.builder()
                    .username("humen1234")
                    .password(encoder.encode("humen1234*"))
                    .roles("ROLE_ADMIN")
                    .accountNonLocked(false)
                    .status(MemberStatus.HUMAN)
                    .build();
            user.addLastLoginTime();
            em.persist(user);
            cartService.createCart(user);
        }
        public void initItemTestV1(){
            for (int i = 1; i <=10 ; i++) {
                Item item = new Item();
                item.setPrice(100);
                item.setName("item"+i);
                item.setQuantity(i);
                em.persist(item);
            }
        }

        public void initCacheDB(){
            memberRepository.findAll().stream().forEach(cacheMemberRepository::save);
        }
    }

}