package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.payment.constant.SafeKingPaymentConst;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.siot.IamportRestClient.response.Payment;


import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.*;

public enum WebhookResponseType {
    /**
     * 포트원 웹훅(webhook)은 다음과 같은 경우에 호출됩니다.
     * 결제가 승인되었을 때(모든 결제 수단) - (status : paid)
     * 가상계좌가 발급되었을 때 - (status : ready)
     * 가상계좌에 결제 금액이 입금되었을 때 - (status : paid)
     * 예약결제가 시도되었을 때 - (status : paid or failed)
     * 관리자 콘솔에서 결제 취소되었을 때 - (status : cancelled)
     *
     * 결제상태.
     * ready: 미결제,
     * paid: 결제완료,
     * cancelled: 결제취소,
     * failed: 결제실패 = ['ready', 'paid', 'cancelled', 'failed'],
     */
    // 가상계좌 비지니스 현재 없음
    ready {
        @Override
        public void changePaymentAndOrderByWebhook(PaymentWebhookRequest request, Payment response, SafekingPayment findSafekingPayment, IamportServiceSubMethod iamportServiceSubMethod) {
            // 결제 상태 변경(미결제)
            findSafekingPayment.changeSafekingPayment(READY, response);

            // 주문 상태 변경(주문대기)
            Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
            findOrder.changeOrderStatus(OrderStatus.READY);

            findOrder.changeSafekingPayment(findSafekingPayment);
        }
    },
    paid {
        @Override
        public void changePaymentAndOrderByWebhook(PaymentWebhookRequest request, Payment response, SafekingPayment findSafekingPayment, IamportServiceSubMethod iamportServiceSubMethod) {
            // 결제 상태 변경(결제 완료)
            findSafekingPayment.changeSafekingPayment(PAID, response);

            // 주문 상태 변경(주문 완료)
            Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
            findOrder.changeOrderStatus(OrderStatus.COMPLETE);

            findOrder.changeSafekingPayment(findSafekingPayment);
        }
    },
    cancelled {
        @Override
        public void changePaymentAndOrderByWebhook(PaymentWebhookRequest request, Payment response, SafekingPayment findSafekingPayment, IamportServiceSubMethod iamportServiceSubMethod) {

            Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());

            if(!findOrder.getStatus().equals(OrderStatus.CANCEL)) {
                // 주문 상태 변경(주문 취소)
                findOrder.cancel(SafeKingPaymentConst.PAYMENT_CANCEL_ADMIN_WEBHOOK);
            }
            // 배송 상태 변경(배송 취소)
            Delivery findDelivery = iamportServiceSubMethod.cancelDelivery(findOrder.getDelivery().getId());

            // 결제 상태 변경(결제 취소)
            findSafekingPayment.changeSafekingPayment(CANCEL, response);

            // 연관관계 반영
            findOrder.changeSafekingPayment(findSafekingPayment);
            findOrder.changeDelivery(findDelivery);
        }
    },
    failed {
        @Override
        public void changePaymentAndOrderByWebhook(PaymentWebhookRequest request, Payment response, SafekingPayment findSafekingPayment, IamportServiceSubMethod iamportServiceSubMethod) {
            // 결제 상태 변경(결제 실패)
            findSafekingPayment.changeSafekingPayment(FAILED, response);

            // 주문 상태 변경(주문 취소)
            Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
            findOrder.changeOrderStatus(OrderStatus.CANCEL);
            findOrder.changeOrderCancelReason(response.getCancelReason());

            findOrder.changeSafekingPayment(findSafekingPayment);
        }
    };

    public abstract void changePaymentAndOrderByWebhook(PaymentWebhookRequest request, Payment response, SafekingPayment findSafekingPayment, IamportServiceSubMethod iamportServiceSubMethod);
}
