package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
}
