package com.safeking.shop.global.auth;

import com.safeking.shop.domain.user.domain.entity.RedisMember;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Getter

/**
 * < Spring security 와 Redis 의 통합 >
 * 1. redis repository 에 저장된 redisMember 는 UserDetails 로 감싸져서
 * 2. 스프링 시큐리티 세션안에 Authenticate 영역에 들어가게 된다.
 **/
public class PrincipalDetailsRedis implements UserDetails {

    private RedisMember member;
    public PrincipalDetailsRedis(RedisMember member){
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
        return "";
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
}