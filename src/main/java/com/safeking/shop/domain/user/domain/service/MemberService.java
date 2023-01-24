package com.safeking.shop.domain.user.domain.service;

import com.safeking.shop.domain.cart.domain.entity.Cart;
import com.safeking.shop.domain.cart.domain.repository.CartItemRepository;
import com.safeking.shop.domain.cart.domain.repository.CartRepository;
import com.safeking.shop.domain.cart.domain.service.CartService;
import com.safeking.shop.domain.item.domain.entity.ItemQuestion;
import com.safeking.shop.domain.item.domain.repository.ItemAnswerRepository;
import com.safeking.shop.domain.item.domain.repository.ItemQuestionRepository;
import com.safeking.shop.domain.order.domain.service.OrderService;
import com.safeking.shop.domain.user.domain.entity.MemberStatus;
import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.GeneralMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.entity.member.OauthMember;
import com.safeking.shop.domain.user.domain.repository.MemberRedisRepository;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryDormantRepository;
import com.safeking.shop.domain.user.domain.repository.MemoryMemberRepository;
import com.safeking.shop.domain.user.domain.service.dto.*;
import com.safeking.shop.global.config.CustomBCryPasswordEncoder;
import com.safeking.shop.global.exception.AgreementException;
import com.safeking.shop.global.exception.MemberNotFoundException;
import com.safeking.shop.global.oauth.provider.GoogleUserInfo;
import com.safeking.shop.global.oauth.provider.KakaoUserInfo;
import com.safeking.shop.global.oauth.provider.Oauth2UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final MemoryMemberRepository memoryMemberRepository;
    private final CustomBCryPasswordEncoder encoder;
    private final CartService cartService;
    private final MemberRedisRepository cacheMemberRepository;
    private final MemoryDormantRepository dormantRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ItemQuestionRepository questionRepository;
    private final ItemAnswerRepository answerRepository;
    private final OrderService orderService;
    private final MemberRedisRepository redisRepository;


    public Long addCriticalItems(CriticalItemsDto criticalItemsDto){

        GeneralMember generalMember = GeneralMember.builder()
                .username(criticalItemsDto.getUsername())
                .password(encoder.encode(criticalItemsDto.getPassword()))
                .email(criticalItemsDto.getEmail())
                .accountNonLocked(true)
                .status(MemberStatus.COMMON)
                .roles("ROLE_USER")
                .build();

        generalMember.addLastLoginTime();
        memoryMemberRepository.save(generalMember);

        return generalMember.getId();
    }
    /**
     * Client Credentails Grant Type 방식
     **/
    public CheckSignUp socialLogin(String registrationId, Map<String, Object> data) {

        Oauth2UserInfo oauth2UserInfo = null;

        if (registrationId.equals("google")) {
            log.info("google login request");

            oauth2UserInfo = new GoogleUserInfo(data);
        } else if (registrationId.equals("kakao")) {
            log.info("Kakao login request");

            oauth2UserInfo = new KakaoUserInfo(data);
        } else {
            throw new IllegalArgumentException("카카오와 구글만 지원합니다.");
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
                    .password(encoder.encode(password))
                    .email(email)
                    .roles(role)
                    .accountNonLocked(true)
                    .status(MemberStatus.COMMON)
                    .provider(provider)
                    .providerId(providerId)
                    .build();

            oauthMember.addLastLoginTime();
            memoryMemberRepository.save(oauthMember);

            return CheckSignUp.createSignUpUser(oauthMember.getId(),false);
        } else if (!oauthMember.getAccountNonLocked()) {

            oauthMember.addCriticalItemsForDormant(encoder.encode(password),email);
            return CheckSignUp.createDormant(dormantRepository.save(oauthMember),false,true);
        } else {
            log.info("Oauth 를 톤해 회원가입을 한 적이 있다.");
            return CheckSignUp.createLoginUser(oauthMember.getUsername(),true);
        }
    }

    public Long addAuthenticationInfo(Long id,AuthenticationInfoDto authenticationInfoDto){

        Member member = memoryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        member.addAuthenticationInfo(
                authenticationInfoDto.getName()
                ,authenticationInfoDto.getBirth()
                ,authenticationInfoDto.getPhoneNumber()
        );

        return member.getId();
    }

    public Long addMemberInfo(Long id, MemberInfoDto memberInfoDto){
        Member member = memoryMemberRepository.findById(id)
                .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

        member.addMemberInfo(
                memberInfoDto.getCompanyName()
                ,memberInfoDto.getCompanyRegistrationNumber()
                ,memberInfoDto.getCorporateRegistrationNumber()
                ,memberInfoDto.getRepresentativeName()
                ,memberInfoDto.getAddress()
                ,memberInfoDto.getContact());

        return member.getId();
    }

    public Long changeMemoryToDB(Long id, Boolean agreement){

        try{
            if(!agreement)throw new AgreementException("약관 동의를 하지 않았습니다.");

            Member member = memoryMemberRepository.findById(id)
                    .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

            member.addAgreement(true);
            //필요한 게 다 있는지 check 하는 로직
            if(!member.isCheckedItem())throw new IllegalArgumentException("필수 항목들을 모두 기입해주세요");
            member.changeId(null);

            //1. db에 저장, 2. 장바구니 생성, 3. 캐시 db에 저장
            memberRepository.save(member);
            cartService.createCart(member);
            cacheMemberRepository.save(new RedisMember(member.getRoles(),member.getUsername()));

            return member.getId();

        }finally {
            memoryMemberRepository.delete(id);
        }
    }




    public boolean idDuplicationCheck(String username){
        //id를 사용가능하다면  true
        return memberRepository.findByUsername(username)
                .orElse(null) == null & memoryMemberRepository.findDuplication(username);
    }

    public void updateMemberInfo(String username,MemberUpdateDto memberUpdateDto){
        log.info("회원 정보 수정");

        memberRepository.findByUsername(username)
                .orElseThrow(()->new MemberNotFoundException("member not found"))
                .updateInfo(
                        memberUpdateDto.getName()
                        ,memberUpdateDto.getBirth()
                        ,memberUpdateDto.getEmail()
                        ,memberUpdateDto.getRepresentativeName()
                        ,memberUpdateDto.getPhoneNumber()
                        ,memberUpdateDto.getCompanyRegistrationNumber()
                        ,memberUpdateDto.getCorporateRegistrationNumber()
                        ,memberUpdateDto.getAddress());
    }

    public void updatePassword(String username, String previousPassword,String password){
            Member member = findMember(username);

            if (!encoder.matches(previousPassword, member.getPassword())) throw new IllegalArgumentException("기존 비밀번호와 일치하지 않습니다.");

            member.updatePassword(encoder.encode(password));
    }

    public void humanAccountConverterBatch(){
        memberRepository.findAll().stream()
                .filter(member -> !member.getRoleList().stream().findFirst().get().equals("ROLE_ADMIN"))
                .forEach(member -> member.convertHumanAccount());

    }



    public Long revertCommonAccounts(Long id, Boolean agreement){

        memberRepository.findById(id).orElseThrow(()->new MemberNotFoundException("회원을 찾을 수가 없습니다.")).revertCommonAccounts();

        try{
            if(agreement!=true)throw new IllegalArgumentException("약관 동의를 하지 않았습니다.");

            Member member = memoryMemberRepository.findById(id).orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));

            member.addAgreement(true);
            //필요한 게 다 있는지 check하는 로직 추가
            if(!member.isCheckedItem())throw new IllegalArgumentException("필수 항목들을 모두 기입해주세요");
            member.changeId(null);
            memberRepository.save(member);

            return member.getId();
        }finally {
            memoryMemberRepository.delete(id);
        }
    }

    public void changeToWithDrawlStatusByUser(String inputUsername, String password, String username) {
        Member member = findMember(username);

        if (!member.getUsername().equals(inputUsername)) throw new IllegalArgumentException("아이디가 일치하지 않습니다.");
        if (!encoder.matches(password, member.getPassword())) throw new IllegalArgumentException("비밀번호가 일치 하지 않습니다.");

        member.changeToWithDrawlStatus();

        RedisMember redisMember = redisRepository.findByUsername(username).orElse(null);
        if (redisMember != null) logout(username);
    }

    public void changeToWithDrawlStatus(String username) {
        findMember(username).changeToWithDrawlStatus();

        RedisMember redisMember = redisRepository.findByUsername(username).orElse(null);
        if (redisMember != null) logout(username);
    }


    public boolean checkAuthority(String username){
        return findMember(username).getRoles().contains("ROLE_ADMIN");
    }

    public void withdrawal(String username) {
        // member
        Member member = findMember(username);

        // 1. 장바구니 관련 삭제
        Cart cart = cartRepository
                .findCartByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("장바구니가 존재하지 않습니다."));

        // 1_1. cartItem 삭제
        cartItemRepository.deleteCartItemBatch(cart.getId());
        // 1_2. cart 삭제
        cartRepository.delete(cart);

        // 2. qna 관련 삭제
        List<ItemQuestion> questionList = questionRepository.findByWriter(member);

        // 2-1. answer 삭제 question 삭제
        answerRepository.deleteByTargetBatch(questionList);
        questionRepository.deleteByTargetBatch(questionList);

        // 3. 주문관련 삭제
        orderService.deleteByMemberBatch(member);

        // 4. member 삭제
        memberRepository.delete(member);
    }

    public void logout(String username) {
        RedisMember redisMember = redisRepository
                .findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("redis member not found"));

        redisRepository.delete(redisMember);
    }

    public String sendTemporaryPassword(String username, String email){
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("아이디와 일치하는 회원이 없습니다."));

        if (!member.getEmail().equals(email)) throw new IllegalArgumentException("회원님이 입력하신 이메일과 작성하신 이메일이 일치하지 않습니다");

        String temporaryPassword = createCode();

        member.changePassword(encoder.encode(temporaryPassword));
        return temporaryPassword;
    }

    //임시비밀번호를 생성
    private static String createCode() {
        Random rand  = new Random();
        String code = "";
        for(int i=0; i<7; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            code+=ran;
        }
        return code;
    }

    private Member findMember(String username) {
        Member member = memberRepository
                .findByUsername(username)
                .orElseThrow(() -> new MemberNotFoundException("회원이 없습니다."));
        return member;
    }

}