//package com.safeking.shop.global.security.oauth2.service;
//
//import com.safeking.shop.global.security.oauth2.user.OAuth2UserInfoFactory;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Service
//@RequiredArgsConstructor
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        ClientRegistration clientRegistration = userRequest.getClientRegistration();
//        String registrationId = clientRegistration.getRegistrationId();
//        String userNameAttributeName = clientRegistration.getProviderDetails()
//                .getUserInfoEndpoint()
//                .getUserNameAttributeName();
//        Map<String, Object> userAttributes = oAuth2User.getAttributes();
//        OAuth2UserInfoFactory.getOAuthUserInfo(userAttributes)
//    }
//}
