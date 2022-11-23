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
public class PrincipalDetails implements UserDetails, OAuth2User {

    private Member member;

    //userInfo의 정보를 받아온다.
    private Map<String,Object> attributes;

    //일반 유저인경우
    public PrincipalDetails(Member member){
        this.member=member;
    }
    //Oauth 유저인경우
    public PrincipalDetails( Member member, Map<String,Object> attributes ){
        this.member= member;
        this.attributes= attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    //권한을 return 한다.
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
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getName() {
        return null;
    }
}