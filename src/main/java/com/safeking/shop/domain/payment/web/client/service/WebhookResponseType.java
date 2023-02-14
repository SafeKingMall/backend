package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.siot.IamportRestClient.response.Payment;
import com.safeking.shop.domain.payment.web.client.service.IamportServiceImpl.*;

import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.*;

public enum WebhookResponseType {
    /**
     * 결제상태.
     * ready: 미결제,
     * paid: 결제완료,
     * cancelled: 결제취소,
     * failed: 결제실패 = ['ready', 'paid', 'cancelled', 'failed'],
     */
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
    cancled {

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
