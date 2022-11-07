package com.safeking.shop.global.security.oauth2.service.domain;

import com.safeking.shop.domain.user.domain.entity.Member;
import com.safeking.shop.domain.user.domain.entity.SocialAccount;
import com.safeking.shop.domain.user.domain.repository.SocialAccountRepository;
import com.safeking.shop.global.security.oauth2.user.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class OAuth2UserAccountService {

    private final SocialAccountRepository socialAccountRepository;

    public MemberAuthenticationInfo saveOrUpdateSocialAccount(OAuth2UserInfo oAuth2UserInfo) {
        Optional<SocialAccount> optionalSocialAccount = socialAccountRepository.findByOauthIdAndProviderFetchMember(
                oAuth2UserInfo.getOAuthId(),
                oAuth2UserInfo.getProvider()
        );

        SocialAccount socialAccount;
        if (optionalSocialAccount.isPresent()) {
            socialAccount = optionalSocialAccount.get();
            socialAccount.updateOAuthInfo(oAuth2UserInfo.getEmail(), oAuth2UserInfo.getName());
        } else {
            socialAccount = socialAccountRepository.save(
                    SocialAccount.builder()
                            .oauthId(oAuth2UserInfo.getOAuthId())
                            .provider(oAuth2UserInfo.getProvider())
                            .email(oAuth2UserInfo.getEmail())
                            .name(oAuth2UserInfo.getName())
                            .build()
            );
        }

        Member member = socialAccount.getMember();
        return new MemberAuthenticationInfo(member.getId(), member.getStatus());
    }
}
