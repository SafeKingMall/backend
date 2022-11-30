package com.safeking.shop.domain.user.web.controller;

import com.safeking.shop.domain.coolsms.web.query.service.SMSService;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.service.CacheService;
import com.safeking.shop.domain.user.domain.service.DormantMemberService;
import com.safeking.shop.domain.user.domain.service.MemberService;
import com.safeking.shop.domain.user.domain.service.dto.CheckSignUp;
import com.safeking.shop.domain.user.web.query.repository.MemberQueryRepository;
import com.safeking.shop.domain.user.web.query.service.MemberQueryService;
import com.safeking.shop.domain.user.web.request.*;
import com.safeking.shop.domain.user.web.request.signuprequest.AgreementInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.AuthenticationInfo;
import com.safeking.shop.domain.user.web.request.signuprequest.CriticalItems;
import com.safeking.shop.domain.user.web.request.signuprequest.MemberInfo;
import com.safeking.shop.domain.user.web.response.MemberDetails;
import com.safeking.shop.domain.user.web.response.MemberListDto;
import com.safeking.shop.global.Error;
import com.safeking.shop.global.auth.PrincipalDetails;
import com.safeking.shop.global.exception.MemberNotFoundException;
import com.safeking.shop.global.jwt.TokenUtils;
import com.safeking.shop.global.jwt.Tokens;
import com.safeking.shop.global.oauth.provider.GoogleUserInfo;
import com.safeking.shop.global.oauth.provider.KakaoUserInfo;
import com.safeking.shop.global.oauth.provider.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.safeking.shop.global.jwt.TokenUtils.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/")
public class MemberController {

    private final MemberService memberService;
    private final MemberQueryService memberQueryService;
    private final MemberRepository memberRepository;
    private final MemberQueryRepository memberQueryRepository;
    private final TokenUtils tokenUtils;
    private final SMSService smsService;
    private final DormantMemberService dormantMemberService;
    private final CacheService cacheService;

    @PostMapping("/signup/criticalItems")
    public Long signUpCriticalItems(@RequestBody @Validated CriticalItems criticalItems) {

        return memberService.addCriticalItems(criticalItems.toServiceDto());
    }

    @PostMapping("/signup/authenticationInfo/{memberId}")
    public Long signUpAuthenticationInfo(@PathVariable Long memberId, @RequestBody @Validated AuthenticationInfo authenticationInfo) {

        return memberService.addAuthenticationInfo(memberId, authenticationInfo.toServiceDto());

    }

    @PostMapping("/signup/memberInfo/{memberId}")
    public Long signUpMemberInfo(@PathVariable Long memberId, @RequestBody @Validated MemberInfo memberInfo) {

        return memberService.addMemberInfo(memberId, memberInfo.toServiceDto());

    }

    @PostMapping("/signup/agreementInfo/{memberId}")
    public Long signUpAgreementInfo(@PathVariable Long memberId, @RequestBody @Validated AgreementInfo agreementInfo) {

        Boolean agreement = null;
        agreement = agreementInfo.getInfoAgreement() & agreementInfo.getUserAgreement();

        return memberService.changeMemoryToDB(memberId, agreement);

    }

    @PostMapping("/dormant/criticalItems")
    public Long dormantCriticalItems(@RequestBody @Validated CriticalItems criticalItems) {

        return dormantMemberService.addCriticalItems(criticalItems.toServiceDto());
    }

    @PostMapping("/dormant/authenticationInfo/{memberId}")
    public Long dormantAuthenticationInfo(@PathVariable Long memberId, @RequestBody @Validated AuthenticationInfo authenticationInfo) {

        return dormantMemberService.addAuthenticationInfo(memberId, authenticationInfo.toServiceDto());

    }

    @PostMapping("/dormant/memberInfo/{memberId}")
    public Long dormantMemberInfo(@PathVariable Long memberId, @RequestBody @Validated MemberInfo memberInfo) {

        return dormantMemberService.addMemberInfo(memberId, memberInfo.toServiceDto());

    }

    @PostMapping("/dormant/agreementInfo/{memberId}")
    public Long dormantAgreementInfo(@PathVariable Long memberId, @RequestBody @Validated AgreementInfo agreementInfo) {

        Boolean agreement = null;

        agreement = agreementInfo.getInfoAgreement() & agreementInfo.getUserAgreement();

        return dormantMemberService.revertCommonAccounts(memberId, agreement);

    }


    @GetMapping("/user/details")
    public MemberDetails showMemberDetails(HttpServletRequest request) {
        return memberQueryService.showMemberDetails(TokenUtils.getUsername(request));
    }

    @PutMapping("/user/update")
    public void update(@RequestBody @Validated UpdateRequest updateRequest, HttpServletRequest request) {
        memberService.updateMemberInfo(TokenUtils.getUsername(request), updateRequest.toServiceDto());
    }

    @PatchMapping("/user/update/password")
    public void updatePassword(@RequestBody @Validated UpdatePWRequest updatePWRequest, HttpServletRequest request) {
        memberService.updatePassword(TokenUtils.getUsername(request), updatePWRequest.getPassword());
    }

    @PostMapping("/id/duplication")
    public boolean idDuplicationCheck(@RequestBody @Validated IdDuplicationRequest idDuplicationRequest) {

        return memberService.idDuplicationCheck(idDuplicationRequest.getUsername());
    }

    @PostMapping("/id/find")
    public ResponseEntity idFind(@RequestBody @Validated IdFindRequest request) {
        if (smsService.checkCode(request.getCode(), request.getClientPhoneNumber())) {

            return new ResponseEntity<>(memberRepository.findByPhoneNumber(request.getClientPhoneNumber())
                    .orElseThrow(() -> new MemberNotFoundException("등록된 휴대번호와 일치하는 회원이 없습니다."))
                    .getUsername(), HttpStatus.OK);
        }

        return ResponseEntity.badRequest().body(new Error(1333, "코드가 일치하지 않습니다."));

    }

    @PostMapping("/temporaryPassword")
    public String sendTemporaryPassword(@RequestBody @Validated PWFindRequest pwFindRequest) {
        return memberService.sendTemporaryPassword(pwFindRequest.getUsername());
    }

    @GetMapping("/admin/cache/restoration")
    public void cacheRestoration(){ cacheService.cacheRestoration(); }

    @GetMapping("/admin/member/list")
    public Page<MemberListDto> showMemberList(String name, @PageableDefault(page = 0, size = 15) Pageable pageable) {

        return memberQueryRepository.searchAllCondition(name, pageable);
    }

    @GetMapping("/admin/humanBatch")
    public void humanConverterBatch() {
        memberService.humanAccountConverterBatch();
    }

    @PostMapping("/oauth/{registrationId}")
    public Long socialLogin(@PathVariable String registrationId, @RequestBody Map<String, Object> data, HttpServletResponse response) {

        CheckSignUp checkSignUp = memberService.socialLogin(registrationId, data);

        if(!checkSignUp.isCheck()){return checkSignUp.getId();}

        //jwt 발행
        Authentication authentication = createAuthentication(checkSignUp.getUsername());

        Tokens tokens = tokenUtils.createTokens(authentication);

        response.addHeader(AUTH_HEADER, BEARER + tokens.getJwtToken());
        response.addHeader(REFRESH_HEADER, tokens.getRefreshToken());

        return null;
    }

    private Authentication createAuthentication(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow();

        PrincipalDetails principalDetails = new PrincipalDetails(member);

        Authentication authentication
                = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }


}