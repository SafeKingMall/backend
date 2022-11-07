package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member,Long> {

    Optional<Member>findByUsername(String username);

}
