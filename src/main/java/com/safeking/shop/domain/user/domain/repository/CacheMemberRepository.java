package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class CacheMemberRepository {
    private static Map<Long, Member> store = new ConcurrentHashMap<>(); //static 사용

    public Long save(Member member) {
        store.put(member.getId(), member);

        return member.getId();
    }
    public Optional<Member> findById(Long id) {

        return Optional.ofNullable(store.get(id));

    }
    public Optional<Member> findByUsername(String username){
        return store.values().stream()
                .filter(member -> member.getUsername().equals(username))
                .findFirst();
    }

    public void update(Long id,Member member){
        store.replace(id,member);
    }

    public void delete(Long id){store.remove(id);}

    public void clearStore() {
        store.clear();
    }
}
