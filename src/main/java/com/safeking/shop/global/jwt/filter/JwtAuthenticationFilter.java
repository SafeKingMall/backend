package com.safeking.shop.global.jwt.filter;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.jwt.filter.dto.LoginRequestDto;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.safeking.shop.global.exhandler.erroconst.ErrorConst.LOGIN_EX_CODE;
import static com.safeking.shop.global.exhandler.erroconst.ErrorConst.LOGIN_LOCK_EX_CODE;
import static com.safeking.shop.global.jwt.TokenUtils.*;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * 1. 로그인이 진행되는 filter
     * 2. 계정이 잠긴 경우, 로그인 정보가 안 맞는 경우 각 모두 상이한 custom error response 객체를 응답
     * 3. 로그인 성공 시에 jwtToken 과 refreshToken 이 나감
     **/
    private final AuthenticationManager authenticationManager;
    private final TokenUtils tokenUtils;
    private ObjectMapper om;
    private MemberRedisRepository memberRepository;


    public JwtAuthenticationFilter(
            AuthenticationManager authenticationManager
            ,TokenUtils tokenUtils
            ,MemberRedisRepository memberRepository
            ) {

        this.authenticationManager = authenticationManager;
        this.tokenUtils = tokenUtils;
        this.memberRepository=memberRepository;

        setFilterProcessesUrl("/api/v1/login");
    }

    //로그인 시에 실행이 되는 필터
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request
            , HttpServletResponse response
    ) throws AuthenticationException {
        log.info("attemptAuthentication 실행");

        try {
            // 1. username,password 를 받아서
            om = new ObjectMapper();
            LoginRequestDto member=om.readValue(request.getInputStream(), LoginRequestDto.class);

            // 2. 바탕으로 토큰을 만들어서
            UsernamePasswordAuthenticationToken authenticationToken
                    = new UsernamePasswordAuthenticationToken(member.getUsername(), member.getPassword());

            //3. loadByUsername 을 실행
            Authentication authentication
                    = authenticationManager.authenticate(authenticationToken);

            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            Member loginMember = principalDetails.getMember();

            RedisMember findRedisMember = memberRepository
                    .findByUsername(member.getUsername())
                    .orElse(null);

            if(findRedisMember==null) {
                //4. redis 의 집어넣기
                memberRepository.save(new RedisMember(loginMember.getRoles(),loginMember.getUsername()));
            }

            return authentication;

        } catch (LockedException e){
            // 계정이 잠긴 경우 custom error response
            generateResponseData(response,403, new Error(LOGIN_LOCK_EX_CODE, e.getMessage()));
        } catch (Exception e) {
            generateResponseData(response,401, new Error(LOGIN_EX_CODE, e.getMessage()));
        }
        return null;
    }
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request
            , HttpServletResponse response
            , FilterChain chain
            , Authentication authResult) throws IOException
    {
        log.info("일반로그인 user 에게 JWT 토큰 발행");
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        String roles = principalDetails.getMember().getRoles();

        Tokens tokens = tokenUtils.createTokens(authResult);

        //header 에 추가
        response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
        response.addHeader(REFRESH_HEADER,BEARER+tokens.getRefreshToken());
        generateResponseData(response, 200, roles);
    }


    private void generateResponseData(
            HttpServletResponse response
            , int httpStatusCode
            , Object object
    ) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(httpStatusCode);

        om.writeValue(response.getWriter(), object);
    }
}