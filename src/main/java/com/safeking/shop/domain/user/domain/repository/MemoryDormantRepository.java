package com.safeking.shop.domain.user.domain.repository;

import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryDormantRepository {
    private static Map<Long, Member> store = new ConcurrentHashMap<>(); //static 사용
    private static AtomicLong sequence = new AtomicLong(0);; //static 사용

    public Long save(Member member) {
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
    public List<Member> findAll(){
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        sequence=new AtomicLong(0);
        store.clear();
    }
}
