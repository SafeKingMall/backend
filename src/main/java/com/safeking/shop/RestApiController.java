package com.safeking.shop;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import com.safeking.shop.domain.user.domain.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestApiController {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final MemberRepository memberRepository;

    //임시 join 컨트롤러
//    @PostMapping("/join")
//    public String join(@RequestBody Member member){
//        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
//        member.setRoles("ROLE_USER");
//        memberRepository.save(member);
//        return "회원가입 완료";
//    }

    //권한 처리 확인 용 임시 controller
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }

//    @GetMapping("/auth/success")
//    public ResponseEntity<?> signinSuccess(HttpServletResponse response) {
//        //임시로 log 를 찍음
//        log.info("JWT 토큰= {}",response.getHeader("Authorization"));
//
//        return ResponseEntity.ok("ok");
//    }

}
