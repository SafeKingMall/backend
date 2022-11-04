package com.safeking.shop.global.security.filter;

import com.safeking.shop.global.jwt.JwtClaims;
import com.safeking.shop.global.jwt.JwtManager;
import com.safeking.shop.global.jwt.JwtPrincipal;
import com.safeking.shop.global.jwt.JwtRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtManager jwtManager;
    private final JwtRepository jwtRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // TODO Access token 만료시 만료 응답 필요
        String accessToken = resolveAccessToken(request);
        if (isValidAccessToken(accessToken)) {
            JwtClaims jwtClaims = jwtManager.getJwtClaims(accessToken);

            JwtPrincipal jwtPrincipal = new JwtPrincipal(accessToken, jwtClaims.getExpiry(), jwtClaims.getUserId());
            List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(jwtClaims.getAuth()));
            Authentication authentication = new UsernamePasswordAuthenticationToken(jwtPrincipal, null, authorities);

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isValidAccessToken(String accessToken) {
        return accessToken != null
                && jwtManager.validationToken(accessToken)
                && jwtRepository.findBlackListTokenByToken(accessToken).isEmpty();
    }

    private String resolveAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }
}
