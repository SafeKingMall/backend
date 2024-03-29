package com.safeking.shop.domain.coolsms.domain.respository;

import com.safeking.shop.domain.coolsms.domain.entity.CoolSMS;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SMSMemoryRepository {
    private static Map<Long, CoolSMS> store = new ConcurrentHashMap<>(); //static 사용
    private static AtomicLong sequence = new AtomicLong(0); //static 사용

    public Long save(CoolSMS coolSMS) {
        coolSMS.changeId(sequence.incrementAndGet());
        store.put(coolSMS.getId(), coolSMS);

        return coolSMS.getId();
    }
    public Optional<CoolSMS> findById(Long id) {return Optional.ofNullable(store.get(id)); }

    public void update(Long id,CoolSMS coolSMS){
        store.replace(id,coolSMS);
    }

    public void delete(Long id){store.remove(id);}

    public void clearStore() {
        store.clear();
    }
    public List<CoolSMS> findAll(){
        return new ArrayList<>(store.values());
    }


    public CoolSMS findByClientPhoneNumber(String clientPhoneNumber) {
        List<CoolSMS> collect = store.values()
                .stream()
                .filter(coolSMS -> coolSMS.getClientPhoneNumber().equals(clientPhoneNumber)).collect(Collectors.toList());

        if (collect.size() - 1 < 0) throw new IllegalArgumentException("등록된 전화번호가 없습니다.");

        return collect.get(collect.size() - 1);
    }
    public Optional<CoolSMS> findByCode(String code) {
        return store.values()
                .stream()
                .filter(coolSMS -> coolSMS.getCode().equals(code)).findFirst();

    }


}
