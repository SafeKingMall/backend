package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.user.domain.service.NormalAccountService;
import com.safeking.shop.domain.user.web.query.service.NormalAccountQueryService;
import com.safeking.shop.domain.user.web.query.service.dto.UserAuthInfo;
import com.safeking.shop.domain.user.web.request.LoginRequest;
import com.safeking.shop.domain.user.web.request.SignUpRequest;
import com.safeking.shop.domain.user.web.response.LoginResponse;
import com.safeking.shop.domain.user.web.response.SignUpResponse;
import com.safeking.shop.global.jwt.JwtManager;
import com.safeking.shop.global.jwt.JwtRepository;
import com.safeking.shop.global.jwt.JwtToken;
import com.safeking.shop.global.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
import java.time.Instant;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final JwtManager jwtManager;
    private final JwtRepository jwtRepository;
    private final NormalAccountService normalAccountService;
    private final NormalAccountQueryService normalAccountQueryService;

    @PostMapping("/sign-up")
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        normalAccountService.addNormalAccount(request.getLoginId(), request.getPassword(), request.getEmail());
        return new SignUpResponse();
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request,
                               HttpServletResponse response) {
        /*
        TODO
            로그인이 되어 있는 상태에서 또 진행하면 AccessToken이 또 발급됨.
            RefreshToken은 서버에서 관리하지만 AccessToken은 블랙리스트에 등록하지 않는 이상 관리하지 않음.
            보안상 문제가 될 수 있다..
        */
        UserAuthInfo userAuthInfo = normalAccountQueryService.getUserAuthInfoByLoginIdAndPassword(
                request.getLoginId(),
                request.getPassword()
        );

        JwtToken accessToken = jwtManager.createAccessToken(userAuthInfo.getUserId(), userAuthInfo.getStatus());

        JwtToken refreshToken = jwtManager.createRefreshToken();
        Duration refreshTokenExpiryDuration = Duration.between(Instant.now(), refreshToken.getExpiry());
        jwtRepository.addRefreshToken(
                userAuthInfo.getUserId(),
                refreshToken.getToken(),
                refreshTokenExpiryDuration
        );
        CookieUtils.addCookie(
                response,
                CookieUtils.COOKIE_NAME_REFRESH_TOKEN,
                refreshToken.getToken(),
                (int) refreshTokenExpiryDuration.getSeconds()
        );

        return new LoginResponse(accessToken.getToken(), userAuthInfo.getStatus().isAdditionalAuthRequired());
    }
}
