package com.safeking.shop.global.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.oauth.PrincipalOauth2Service;
import com.safeking.shop.global.jwt.JwtAuthenticationFilter;
import com.safeking.shop.global.jwt.JwtAuthorizationFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@Slf4j
public class SecurityConfig{

    private final CorsConfig corsConfig;
    private final MemberRepository memberRepository;

    private final PrincipalOauth2Service oauth2Service;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(new MyFilter3(), SecurityContextHolderFilter.class);
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .formLogin().disable()
                .httpBasic().disable()

                .apply(new MyCustomDsl())

                .and()
                .authorizeRequests()

                .antMatchers("/api/v1/user/**")
                .access("hasRole('ROLE_USER')or hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")

                .antMatchers("/api/v1/manager/**")
                .access("hasRole('ROLE_MANAGER')or hasRole('ROLE_ADMIN')")

                .antMatchers("/api/v1/admin/**")
                .access("hasRole('ROLE_ADMIN')")

                .anyRequest().permitAll()

                .and()
                .oauth2Login()
                .userInfoEndpoint()
                .userService(oauth2Service)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        log.info("OauthLogin user 에게 JWT 토큰 발행");

                        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

                        String jwtToken = JWT.create()
                                .withSubject(principalDetails.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
                                .withClaim("id", principalDetails.getMember().getId())
                                .withClaim("username", principalDetails.getMember().getUsername())
                                .sign(Algorithm.HMAC512("safeKing"));

                        response.addHeader("Authorization","Bearer "+jwtToken);

                        //토큰을 발행 후에 forward
                        String targetUrl = "/auth/success";
                        RequestDispatcher dis = request.getRequestDispatcher(targetUrl);
                        dis.forward(request, response);
                    }
                })
        ;
        return httpSecurity.build();

    }
    public class MyCustomDsl extends AbstractHttpConfigurer<MyCustomDsl, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) throws Exception {

            AuthenticationManager authenticationManager = http.getSharedObject(AuthenticationManager.class);
            http
                    .addFilter(corsConfig.corsFilter())
                    .addFilter(new JwtAuthenticationFilter(authenticationManager))
                    .addFilter(new JwtAuthorizationFilter(authenticationManager, memberRepository))
            ;

        }
    }
}
