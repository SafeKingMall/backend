package com.safeking.shop.domain.order.domain.service.dto;

import com.safeking.shop.domain.user.domain.entity.OAuthProvider;
import lombok.Builder;
import lombok.Data;

@Data
public class OrderOrderDto {
    private Long memberId;
    private String userId;
    private String oauthId;
    private OAuthProvider provider;
    private Long itemId;
    private String memo;

    @Builder
    public OrderOrderDto(Long memberId, String userId, String oauthId, OAuthProvider provider, Long itemId, String memo) {
        this.memberId = memberId;
        this.userId = userId;
        this.oauthId = oauthId;
        this.provider = provider;
        this.itemId = itemId;
        this.memo = memo;
    }
}
