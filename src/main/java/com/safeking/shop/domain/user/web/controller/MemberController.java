package com.safeking.shop.domain.user.web.controller;

import com.auth0.jwt.JWT;
import com.safeking.shop.domain.coolsms.SMSService;
import com.safeking.shop.domain.coolsms.request.SMSCode;
import com.safeking.shop.domain.coolsms.response.SMSResponse;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.dto.GeneralSingUpDto;
import com.safeking.shop.domain.user.web.request.idDuplication.IdDuplicationRequest;
import com.safeking.shop.domain.user.web.request.idFind.IdFindRequest;
import com.safeking.shop.domain.user.web.request.passwordFind.PWFindRequest;
import com.safeking.shop.domain.user.web.request.signup.SignUpRequest;
import com.safeking.shop.domain.user.web.response.IdFind.IdFindResponse;
import com.safeking.shop.domain.user.web.response.idDuplication.IdDuplicationResponse;
import com.safeking.shop.domain.user.web.response.oauth.OauthResponse;
import com.safeking.shop.domain.user.web.response.passwordFind.PWFindResponse;
import com.safeking.shop.domain.user.web.response.signup.Data;
import com.safeking.shop.domain.user.web.response.signup.SignUpResponse;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.oauth.provider.GoogleUserInfo;
import com.safeking.shop.global.oauth.provider.KakaoUserInfo;
import com.safeking.shop.global.oauth.provider.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.safeking.shop.global.jwt.TokenUtils.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/")
public class MemberController {

    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;
    private final TokenUtils tokenUtils;

    private final SMSService smsService;

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody @Validated SignUpRequest signUpRequest){

        GeneralSingUpDto generalSingUpDto = signUpRequest.toServiceDto();
        memberService.join(generalSingUpDto);

        SignUpResponse signUpResponse = SignUpResponse.builder()
                .code(200)
                .message(SignUpResponse.SUCCESS_MESSAGE)
                .data(new Data(Data.message))
                .error(new Error())
                .build();

        return ResponseEntity.ok().body(signUpResponse);
    }

    @PostMapping("/id/duplication")
    public ResponseEntity<IdDuplicationResponse> idDuplicationCheck(@RequestBody @Validated IdDuplicationRequest idDuplicationRequest){

        boolean idAvailable=true;
        String message=IdDuplicationResponse.SUCCESS_MESSAGE;

        if(!memberService.idDuplicationCheck(idDuplicationRequest.getUsername())){
            idAvailable=false;
            message=IdDuplicationResponse.FAIL_MESSAGE;
        }

        return ResponseEntity.ok()
                .body(IdDuplicationResponse.builder()
                        .code(200)
                        .message(message)
                        .data(new com.safeking.shop.domain.user.web.response.idDuplication.Data(idAvailable))
                        .error(new Error())
                        .build());
    }

    @PostMapping("/id/find")
    public ResponseEntity<IdFindResponse> idFind(@RequestBody @Validated IdFindRequest request){

        int code=400;
        String message=IdFindResponse.FAIL_MESSAGE;
        String username= com.safeking.shop.domain.user.web.response.IdFind.Data.message;
        HttpStatus httpStatus=HttpStatus.BAD_REQUEST;

        if(smsService.checkCode(request.getCode())){
            code=200;
            httpStatus=HttpStatus.OK;
            message=IdFindResponse.SUCCESS_MESSAGE;
            username  = memberRepository
                    .findByPhoneNumber(request.getClientPhoneNumber())
                    .orElseThrow(() -> new IllegalArgumentException("핸드폰 번호와 일치하는 회원 정보가 없습니다."))
                    .getUsername();

        }

        return new ResponseEntity<>(
                IdFindResponse.builder()
                        .code(code)
                        .message(message)
                        .data(new com.safeking.shop.domain.user.web.response.IdFind.Data(username))
                        .error(new Error())
                        .build(),httpStatus);
    }

    @PostMapping("/temporaryPassword")
    public ResponseEntity<PWFindResponse> sendTemporaryPassword(@RequestBody @Validated PWFindRequest pwFindRequest) throws CoolsmsException {
        memberService.sendTemporaryPassword(pwFindRequest.getUsername());

        return ResponseEntity.ok().body(
                PWFindResponse.builder()
                        .code(200)
                        .message(PWFindResponse.SUCCESS_MESSAGE)
                        .data(new com.safeking.shop.domain.user.web.response.passwordFind.Data(""))
                        .error(new Error())
                        .build()
        );
    }


    @PostMapping("/oauth/{registrationId}")
    public ResponseEntity<OauthResponse> socialLogin(@PathVariable String registrationId, @RequestBody Map<String, Object> data, HttpServletResponse response) {

        Oauth2UserInfo oauth2UserInfo = null;

        if (registrationId.equals("google")) {
            log.info("google login request");

            oauth2UserInfo = new GoogleUserInfo(data);
        } else if(registrationId.equals("kakao")) {
            log.info("Kakao login request");

            oauth2UserInfo = new KakaoUserInfo(data);
        } else{
            throw new IllegalArgumentException("카카오와 구글만 지원합니다.");
        }

        String provider = oauth2UserInfo.getProvider();
        String providerId = oauth2UserInfo.getProviderId();
        String name = oauth2UserInfo.getName();
        String username = provider + "_" + providerId;
        String password = encoder.encode("safeking");
        String email = oauth2UserInfo.getEmail();//구글이 준 email
        String role = "ROLE_USER";

        Member oauthMember = memberRepository.findByUsername(username).orElse(null);
        if (oauthMember == null) {
            oauthMember = OauthMember.builder()
                    .username(username)
                    .password(password)
                    .name(name)
                    .email(email)
                    .roles(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            memberRepository.save(oauthMember);
        } else {
            log.info("Oauth 를 톤해 회원가입을 한 적이 있다.");
        }

        //jwt 발행
        if(oauthMember.getUsername()!=null){
            //authentication 을 생성
            Authentication authentication = createAuthentication(username);

            Tokens tokens = tokenUtils.createTokens(authentication);

            response.addHeader(AUTH_HEADER,BEARER+tokens.getJwtToken());
            response.addHeader(REFRESH_HEADER,tokens.getRefreshToken());
        }

        return new ResponseEntity<>(OauthResponse.builder()
                .code(200)
                .message(OauthResponse.SUCCESS_MESSAGE)
                .data(new Data(Data.message))
                .error(new Error())
                .build(), HttpStatus.OK);
    }

    private Authentication createAuthentication(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(principalDetails, null,principalDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }




}
