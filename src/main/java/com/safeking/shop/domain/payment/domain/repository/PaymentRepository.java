package com.safeking.shop.domain.payment.domain.repository;

import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<SafekingPayment, Long> {
}
