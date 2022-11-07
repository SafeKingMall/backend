package com.safeking.shop.global.config;

import com.safeking.shop.global.jwt.JwtManager;
import com.safeking.shop.global.jwt.JwtRepository;
import com.safeking.shop.global.security.filter.JwtAuthenticationFilter;
import com.safeking.shop.global.security.oauth2.handler.CustomOAuth2AuthenticationFailureHandler;
import com.safeking.shop.global.security.oauth2.handler.CustomOAuth2AuthenticationSuccessHandler;
import com.safeking.shop.global.security.oauth2.repository.OAuth2AuthorizationRequestRepository;
import com.safeking.shop.global.security.oauth2.service.CustomOAuth2UserService;
import com.safeking.shop.global.security.role.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtManager jwtManager;
    private final JwtRepository jwtRepository;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;
    private final CustomOAuth2AuthenticationSuccessHandler customOAuth2AuthenticationSuccessHandler;
    private final CustomOAuth2AuthenticationFailureHandler customOAuth2AuthenticationFailureHandler;

    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtManager, jwtRepository);
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> {
            web.ignoring().antMatchers("/h2-console/**");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .oauth2Login()
                .authorizationEndpoint()
                .baseUri("/oauth2/authorization/**")
                .authorizationRequestRepository(oAuth2AuthorizationRequestRepository)

                .and()
                .redirectionEndpoint()
                .baseUri("/oauth2/callback/*")

                .and()
                .userInfoEndpoint()
                .userService(customOAuth2UserService)

                .and()
                .successHandler(customOAuth2AuthenticationSuccessHandler)
                .failureHandler(customOAuth2AuthenticationFailureHandler)

                .and()
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);

        http
                .authorizeRequests()
                .antMatchers("/api/v1/auth/**").permitAll()
                .antMatchers("/api/v1/test/temp").hasRole(Role.TEMP.name())
                .antMatchers("/api/v1/test/active").hasRole(Role.USER.name())
                .antMatchers("/api/v1/test/admin").hasRole(Role.ADMIN.name());

        return http.build();
    }
}
