package com.safeking.shop.domain.order.domain.service.login;

import com.safeking.shop.domain.exception.LoginException;
import com.safeking.shop.domain.user.domain.entity.OAuthProvider;
import com.safeking.shop.domain.user.domain.entity.SocialAccount;
import com.safeking.shop.domain.user.domain.repository.SocialAccountRepository;

import java.util.Optional;

public class SocialLogin implements LoginBehavior {

    private final SocialAccountRepository socialAccountRepository;
    private String oauthId;
    private OAuthProvider provider;

    public SocialLogin(SocialAccountRepository socialAccountRepository, String oauthId, OAuthProvider provider) {
        this.socialAccountRepository = socialAccountRepository;
        this.oauthId = oauthId;
        this.provider = provider;
    }

    @Override
    public LoginDto login() {
        Optional<SocialAccount> findMember = socialAccountRepository.findByOauthIdAndProviderFetchMember(oauthId, provider);
        Optional<LoginDto> loginDto = findMember.map(m -> new LoginDto(m.getMember().getId()));

        return loginDto.orElseThrow(
                () -> new LoginException("해당 회원이 없습니다."));
    }
}
