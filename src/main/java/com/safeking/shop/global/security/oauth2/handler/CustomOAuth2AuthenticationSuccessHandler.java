package com.safeking.shop.global.security.oauth2.handler;

import com.safeking.shop.global.jwt.JwtManager;
import com.safeking.shop.global.jwt.JwtRepository;
import com.safeking.shop.global.jwt.JwtToken;
import com.safeking.shop.global.security.oauth2.repository.OAuth2AuthorizationRequestRepository;
import com.safeking.shop.global.security.oauth2.service.UserIdOAuth2User;
import com.safeking.shop.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtManager jwtManager;
    private final JwtRepository jwtRepository;
    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        UserIdOAuth2User userIdOAuth2User = (UserIdOAuth2User) authentication.getPrincipal();

        String authorities = userIdOAuth2User.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        JwtToken accessToken = jwtManager.createAccessToken(userIdOAuth2User.getUserId(), authorities);
        JwtToken refreshToken = jwtManager.createRefreshToken();
        jwtRepository.addRefreshToken(
                userIdOAuth2User.getUserId(),
                refreshToken.getToken(),
                Duration.between(Instant.now(), refreshToken.getExpiry())
        );

        CookieUtils.addCookie(
                response,
                CookieUtils.COOKIE_NAME_REFRESH_TOKEN,
                refreshToken.getToken(),
                (int) Duration.between(Instant.now(), refreshToken.getExpiry()).getSeconds()
        );

        String redirectUri = CookieUtils.getCookie(request, CookieUtils.COOKIE_NAME_REDIRECT_URI)
                .map(Cookie::getValue)
                .orElseThrow(); // TODO CustomException

        String targetUri = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("token", accessToken.getToken())
                .queryParam("additional-auth-required", userIdOAuth2User.requiredAdditionalAuth())
                .build().toUriString();

        oAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
        response.sendRedirect(targetUri);
    }
}
