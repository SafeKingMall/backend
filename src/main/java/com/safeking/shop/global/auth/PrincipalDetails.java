package com.safeking.shop.global.auth;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

@Getter
//스프링 시큐리티 세션안에 Authenticate 영역
public class PrincipalDetails implements UserDetails {

    private Member member;

    //일반 유저인경우
    public PrincipalDetails(Member member){
        this.member=member;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        Collection<GrantedAuthority>authorities=new ArrayList<>();

        member.getRoleList().forEach(role->{
            authorities.add(()->role);
        });
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //false 면 사용불가
        return member.getAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}