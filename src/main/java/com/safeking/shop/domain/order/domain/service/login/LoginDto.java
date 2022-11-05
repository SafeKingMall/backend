package com.safeking.shop.domain.order.domain.service.login;

import lombok.Builder;
import lombok.Data;

/**
 * 일반 로그인,
 * 소셜 로그인 필드값 통합용 클래스
 */
@Data
public class LoginDto {
    private Long memberId;

    @Builder
    public LoginDto(Long memberId) {
        this.memberId = memberId;
    }
}
