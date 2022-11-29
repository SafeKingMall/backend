//package com.safeking.shop.domain.common;
//
//import com.safeking.shop.global.auth.PrincipalDetails;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.data.domain.AuditorAware;
//import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//
//import java.util.Optional;
//
//@Component
//
//@Slf4j
//public class SpringSecurityAuditor implements AuditorAware<String>
//{
//    @Override
//    public Optional<String> getCurrentAuditor()
//    {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (null == authentication || !authentication.isAuthenticated())return null;
//
//        log.info("--------"+authentication.getPrincipal());
//
//        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
//        return Optional.of(principal.getMember().getUsername());
//    }
//}
