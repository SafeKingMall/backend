//package com.safeking.shop.global.jwt;
//
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.safeking.shop.domain.user.domain.entity.auth.PrincipalDetails;
//import com.safeking.shop.domain.user.domain.entity.member.Member;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Slf4j
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private final AuthenticationManager authenticationManager;
//
//
//    //로그인 시에 실행이 되는 필터
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        log.info("attemptAuthentication 실행");
//
//        try {
//            // 1. username,password를 받아서
//            ObjectMapper om = new ObjectMapper();
//            Member member=om.readValue(request.getInputStream(), Member.class);
//
//            // 2. 바탕으로 토큰을 만들어서
//            UsernamePasswordAuthenticationToken authenticationToken
//                    = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());
//
//            //3. loadByUsername을 실행
//            Authentication authentication
//                    = authenticationManager.authenticate(authenticationToken);
//
//            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
//            log.info("principalDetails.name={}",principalDetails.getMember().getUsername());
//
//            return authentication;
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
//        System.out.println("successfulAuthentication가 실행");
//
//        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
//        String jwtToken = JWT.create()
//                .withSubject(principalDetails.getUsername())
//                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10)))
//                .withClaim("id", principalDetails.getUser().getId())
//                .withClaim("username", principalDetails.getUser().getUsername())
//                .sign(Algorithm.HMAC512("cos"));
//
//        response.addHeader("Authorization","Bearer "+jwtToken);
//    }
//}
