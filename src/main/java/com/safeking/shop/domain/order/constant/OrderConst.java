package com.safeking.shop.domain.order.constant;

public class OrderConst {
    public static final Integer DeliveryCost = 2500;
    public static final String BLANK = "";
    public static final String ORDER_ITEM_NONE = "해당 상품에 대한 정보가 없습니다.";
    public static final String ORDER_MEMBER_NONE = "회원이 없습니다.";
    public static final String ORDER_SUCCESS = "주문 성공";
    public static final String ORDER_FAIL = "주문 실패";
    public static final String ORDER_NONE = "주문이 없습니다.";
    public static final String ORDER_CANCEL_DELIVERY_DONE = "배송중이거나 배송완료된 상품은 취소가 불가합니다.";
    public static final String ORDER_MODIFY_DELIVERY_DONE = "배송 중이거나 배송완료된 상품은 주문(배송) 정보 수정이 불가합니다.";
    public static final String ORDER_CANCEL_SUCCESS = "주문 취소 성공";
    public static final String ORDER_CANCEL_FAIL = "주문 취소 실패";
    public static final String ORDER_MODIFY_SUCCESS = "주문(배송) 정보 수정 성공";
    public static final String ORDER_MODIFY_FAIL = "주문(배송) 정보 수정 실패";
    public static final String ORDER_FIND_SUCCESS = "주문(배송) 정보 조회 성공";
    public static final String ORDER_FIND_FAIL = "주문(배송) 정보 조회 실패";
    public static final String ORDER_DETAIL_FIND_SUCCESS = "주문 상세 조회 완료";
    public static final String ORDER_DETAIL_FIND_FAIL = "주문 상세 조회 실패";
    public static final String ORDER_LIST_FIND_SUCCESS = "주문 다건 조회 성공";
    public static final String ORDER_LIST_FIND_FAIL = "주문 다건 조회 실패";
    public static final String ORDER_LIST_FIND_FAIL_PAYMENT_STATUS = "주문 다건 조회 실패(결제 상태가 올바르지 않습니다.)";
    public static final String ORDER_LIST_FIND_FAIL_DELIVERY_STATUS = "주문 다건 조회 실패(배송 상태가 올바르지 않습니다.)";

    /* 관리자 */
    public static final String ADMIN_ORDER_DETAIL_FIND_SUCCESS = "관리자용 주문 상세 조회 완료";
    public static final String ADMIN_ORDER_DETAIL_FIND_FAIL = "관리자용 주문 상세 조회 실패";
    public static final String ADMIN_ORDER_DETAIL_MODIFY_SUCCESS = "관리자용 주문 상세 수정 성공";
    public static final String ADMIN_ORDER_DETAIL_MODIFY_FAIL = "관리자용 주문 상세 수정 실패";
    public static final String ADMIN_ORDER_LIST_FIND_SUCCESS = "관리자용 주문 다건 조회 성공";
    public static final String ADMIN_ORDER_LIST_FIND_FAIL = "관리자용 주문 상세 조회 실패";
}
