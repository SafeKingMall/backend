package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository {
    private static Map<Long, Member> store = new HashMap<>(); //static 사용
    private static long sequence = 0L; //static 사용

    public Long save(Member member) {

        member.changeId(++sequence);
        store.put(member.getId(), member);

        return member.getId();
    }
    public Optional<Member> findById(Long id) {

        return Optional.ofNullable(store.get(id));

    }

    public void update(Long id,Member member){
        store.replace(id,member);
    }

    public void delete(Long id){store.remove(id);}

    public void clearStore() {
        store.clear();
    }
}
