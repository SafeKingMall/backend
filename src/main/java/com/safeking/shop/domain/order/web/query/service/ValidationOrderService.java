package com.safeking.shop.domain.order.web.query.service;

import com.safeking.shop.domain.user.domain.entity.member.Member;

public interface ValidationOrderService {
    Member validationMember(String username); //회원 검증
}
