package com.safeking.shop.domain.payment.domain.repository;

import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SafekingPaymentRepository extends JpaRepository<SafekingPayment, Long> {
    // 주문 번호 조회
    Optional<SafekingPayment> findByMerchantUid(String merchantUid);
    Optional<SafekingPayment> findByImpUid(String impUid);
}
