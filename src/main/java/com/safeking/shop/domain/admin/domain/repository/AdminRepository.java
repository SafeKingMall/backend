package com.safeking.shop.domain.admin.domain.repository;

import com.safeking.shop.domain.admin.domain.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
