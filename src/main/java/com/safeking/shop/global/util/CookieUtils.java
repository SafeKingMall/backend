package com.safeking.shop.global.util;

import org.springframework.util.SerializationUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

public class CookieUtils {

    public static final String COOKIE_NAME_OAUTH2_AUTHORIZATION_REQUEST = "oauth2-authorizationRequest";
    public static final String COOKIE_NAME_REDIRECT_URI = "redirect-uri";
    public static final String COOKIE_NAME_REFRESH_TOKEN = "refresh-token";

    public static Optional<Cookie> getCookie(HttpServletRequest request, String cookieName) {
        Cookie[] cookies = request.getCookies();
        return cookies == null
                ? Optional.empty()
                : Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals(cookieName))
                .findFirst();
    }

    public static void addCookie(HttpServletResponse response, String cookieName, String cookieValue, int maxAge) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // TODO secure cookie
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String cookieName) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length > 0) {
            for (Cookie cookie : cookies) {
                if (cookieName.equals(cookie.getName())) {
                    cookie.setValue("");
                    cookie.setMaxAge(0);
                    cookie.setHttpOnly(true);
                    cookie.setSecure(false); // TODO secure cookie
                    response.addCookie(cookie);
                }
            }
        }
    }

    public static String serialize(Object obj) {
        return Base64.getUrlEncoder().encodeToString(SerializationUtils.serialize(obj));
    }

    public static <T> T deserialize(Cookie cookie, Class<T> clazz) {
        return clazz.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }
}
