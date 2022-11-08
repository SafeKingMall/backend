package com.safeking.shop.global.oauth;

import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.global.oauth.provider.FaceBookUserInfo;
import com.safeking.shop.global.oauth.provider.GoogleUserInfo;
import com.safeking.shop.global.oauth.provider.NaverUserInfo;
import com.safeking.shop.global.oauth.provider.Oauth2UserInfo;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrincipalOauth2Service extends DefaultOAuth2UserService {

    private final BCryptPasswordEncoder encoder;

    private final MemberRepository memberRepository;

    /**
     * userRequest 에 사용자 정보와 엑세스 토큰이 들어온다.*/
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("loadUser 실행");

        //여기서 정보를 가져온다.
        OAuth2User oAuth2User = super.loadUser(userRequest);

        Oauth2UserInfo oauth2UserInfo = null;

        if (userRequest.getClientRegistration().getRegistrationId().equals("google")) {
            log.info("google login request");

            oauth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            log.info("facebook login request");

            oauth2UserInfo = new FaceBookUserInfo(oAuth2User.getAttributes());
        } else if (userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
            log.info("Naver login request");

            oauth2UserInfo = new NaverUserInfo((Map) oAuth2User.getAttributes().get("response"));
        } else {
            //수정
            log.info("우리는 구글과 네이버와 페이스북만 지원해요");
        }

        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String username = provider + "_" + providerId;
        String password = encoder.encode("safeking");
        String email = oauth2UserInfo.getEmail();//구글이 준 email
        String role = "ROLE_USER";

        Member oauthMember = memberRepository.findByUsername(username).orElse(null);
        if (oauthMember == null) {
            oauthMember = OauthMember.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .roles(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(oauthMember);
        } else {
            log.info("Oauth 를 톤해 회원가입을 한 적이 있다.");
        }
        return new PrincipalDetails(oauthMember,oAuth2User.getAttributes());
    }
}
