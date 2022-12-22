package com.safeking.shop.domain.payment.domain.repository;

import com.safeking.shop.domain.payment.domain.entity.SafeKingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<SafeKingPayment, Long> {
}
