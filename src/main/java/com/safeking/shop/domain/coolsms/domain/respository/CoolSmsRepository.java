package com.safeking.shop.domain.coolsms.domain.respository;

import com.safeking.shop.domain.coolsms.domain.entity.CoolSMS;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CoolSmsRepository extends JpaRepository<CoolSMS,Long> {

    Optional<CoolSMS> findByClientPhoneNumber(String clientPhoneNumber);
}
