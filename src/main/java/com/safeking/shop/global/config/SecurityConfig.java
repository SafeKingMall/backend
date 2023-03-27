package com.safeking.shop.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.filter.JwtAuthenticationExceptionFilter;
import com.safeking.shop.global.jwt.filter.JwtAuthenticationFilter;
import com.safeking.shop.global.jwt.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig{

    private final CorsConfig corsConfig;
    private final MemberRedisRepository memberRepository;
    private final TokenUtils tokenUtils;
    private final ObjectMapper om;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

        httpSecurity.csrf().disable().cors();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .apply(new MyCustomDsl(tokenUtils,om))

                .and()
                .authorizeRequests()

                // 아임포트 웹훅(결제내역 동기화, 모바일 결제 검증시 토큰 이슈(선우님 요청사항))
                .antMatchers("/api/v1/user/payment/webhook", "/api/v1/user/payment")
                .permitAll()

                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER')or hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")

                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")

                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")

                .anyRequest().permitAll()

        ;
        return httpSecurity.build();

    }
    @RequiredArgsConstructor
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        private final TokenUtils tokenUtils;
        private final ObjectMapper om;
        @Override
        public void configure(HttpSecurity http) throws Exception {
            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            JwtAuthorizationFilter jwtAuthorizationFilter = new JwtAuthorizationFilter(authenticationManager, memberRepository);

            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager, tokenUtils, memberRepository))
                    .addFilter(jwtAuthorizationFilter)
                    .addFilterBefore(new JwtAuthenticationExceptionFilter(om),jwtAuthorizationFilter.getClass());
            ;

        }
    }
}