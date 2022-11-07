package com.safeking.shop.global.security.oauth2.repository;

import com.safeking.shop.global.util.CookieUtils;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class OAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final int cookieAge = 60;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        return CookieUtils.getCookie(request, CookieUtils.COOKIE_NAME_OAUTH2_AUTHORIZATION_REQUEST)
                .map(cookie -> CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class))
                .orElse(null);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            return;
        }
        // OAuth2 로그인 요청 정보
        CookieUtils.addCookie(response, CookieUtils.COOKIE_NAME_OAUTH2_AUTHORIZATION_REQUEST, CookieUtils.serialize(authorizationRequest), cookieAge);
        // OAuth2 로그인 성공시 redirect 할 uri
        CookieUtils.addCookie(response, CookieUtils.COOKIE_NAME_REDIRECT_URI, request.getParameter("redirect_uri"), cookieAge);
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, CookieUtils.COOKIE_NAME_OAUTH2_AUTHORIZATION_REQUEST);
        CookieUtils.deleteCookie(request, response, CookieUtils.COOKIE_NAME_REDIRECT_URI);
    }
}
