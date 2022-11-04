package com.safeking.shop.global.security.oauth2.handler;

import com.safeking.shop.global.security.oauth2.repository.OAuth2AuthorizationRequestRepository;
import com.safeking.shop.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomOAuth2AuthenticationFailureHandler implements AuthenticationFailureHandler {

    private final OAuth2AuthorizationRequestRepository oAuth2AuthorizationRequestRepository;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String redirectUri = CookieUtils.getCookie(request, CookieUtils.COOKIE_NAME_REDIRECT_URI)
                .map(Cookie::getValue)
                .orElseThrow(); // TODO CustomException

        log.error("oauth2 login fail.", exception);

        String targetUri = UriComponentsBuilder.fromUriString(redirectUri)
                .queryParam("error", exception.getLocalizedMessage())
                .build().toUriString();

        oAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);

        response.sendRedirect(targetUri);
    }
}
