package com.safeking.shop.domain.coolsms.domain.respository;

import com.safeking.shop.domain.coolsms.domain.entity.CoolSMS;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class SMSMemoryRepository {
    private static Map<Long, CoolSMS> store = new ConcurrentHashMap<>(); //static 사용
    private static AtomicLong sequence = new AtomicLong(0); //static 사용

    public Long save(CoolSMS coolSMS) {
        coolSMS.changeId(sequence.incrementAndGet());
        store.put(coolSMS.getId(), coolSMS);

        return coolSMS.getId();
    }
    public Optional<CoolSMS> findById(Long id) {

        return Optional.ofNullable(store.get(id));

    }

    public void update(Long id,CoolSMS coolSMS){
        store.replace(id,coolSMS);
    }

    public void delete(Long id){store.remove(id);}

    public void clearStore() {
        store.clear();
    }

    public Optional<CoolSMS> findByClientPhoneNumber(String clientPhoneNumber) {
        return store.values().stream().filter(coolSMS -> coolSMS.getClientPhoneNumber().equals(clientPhoneNumber)).findFirst();

    }
    public Optional<CoolSMS> findByCode(String code) {
        return store.values().stream().filter(coolSMS -> coolSMS.getCode().equals(code)).findFirst();

    }


}
