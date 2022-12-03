package com.safeking.shop.domain.order.web.dto.response.admin.search;

import lombok.Builder;
import lombok.Data;

@Data
public class AdminOrderListMemberResponse {
    private String name;

    @Builder
    public AdminOrderListMemberResponse(String name) {
        this.name = name;
    }
}
