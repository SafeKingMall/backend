package com.safeking.shop.domain.payment.domain.repository;

import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SafekingPaymentRepository extends JpaRepository<SafekingPayment, Long> {
    // 주문 번호 조회
    Optional<SafekingPayment> findByMerchantUid(String merchantUid);
    Optional<SafekingPayment> findByImpUid(String impUid);

    @Modifying
    @Query("delete from SafekingPayment sp where sp in :safekingPaymentList")
    void deleteByOrderBatch(@Param("safekingPaymentList")List<SafekingPayment> safekingPaymentList);
}
