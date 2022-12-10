package com.safeking.shop.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.domain.entity.Address;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static com.safeking.shop.global.jwt.TokenUtils.AUTH_HEADER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
@Component
public class TestUserHelper {
    @Autowired
    CustomBCryPasswordEncoder encoder;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberRedisRepository redisRepository;

    public static String USER_USERNAME="testUser1234";
    public static String USER_PASSWORD="testUser1234*";
    public static String USER_ROLE="ROLE_USER";

    public static String ADMIN_USERNAME="admin1234";
    public static String ADMIN_PASSWORD="admin1234*";
    public static String ADMIN_ROLE="ROLE_ADMIN";

    public Member createMember(){
        GeneralMember member = GeneralMember.builder()
                .name("user")
                .birth("birth")
                .username("testUser1234")
                .password(encoder.encode("testUser1234*"))
                .email("email")
                .roles("ROLE_USER")
                .phoneNumber("01012345678")
                .companyName("safeking")
                .companyRegistrationNumber("111")
                .corporateRegistrationNumber("222")
                .representativeName("MS")
                .contact("contact")
                .address(new Address("seoul", "mapo", "111"))
                .agreement(true)
                .accountNonLocked(true)
                .status(MemberStatus.COMMON)
                .build();
        member.addLastLoginTime();
        memberRepository.save(member);

        return member;
    }
    public Member createADMIN(){
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
        memberRepository.save(admin);

        return admin;
    }
}
