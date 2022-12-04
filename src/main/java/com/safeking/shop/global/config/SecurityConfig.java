package com.safeking.shop.global.config;

import com.safeking.shop.domain.user.domain.repository.CacheMemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.filter.JwtAuthenticationFilter;
import com.safeking.shop.global.jwt.filter.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
    private final MemberRepository memberRepository;

    private final TokenUtils tokenUtils;

//    private final PrincipalOauth2Service oauth2Service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(new MyFilter3(), SecurityContextHolderFilter.class);
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .apply(new MyCustomDsl(tokenUtils))

                .and()
                .authorizeRequests()

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

        @Override
        public void configure(HttpSecurity http) throws Exception {

            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager,tokenUtils))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository))
            ;

        }
    }
}