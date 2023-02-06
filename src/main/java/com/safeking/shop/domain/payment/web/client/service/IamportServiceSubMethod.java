package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.exception.PaymentException;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.safeking.shop.domain.order.constant.OrderConst.ORDER_NONE;
import static com.safeking.shop.domain.payment.constant.SafeKingPaymentConst.REFUND_FEE_CHECK;
import static com.safeking.shop.domain.payment.constant.SafeKingPaymentConst.SAFEKING_PAYMENT_NONE;
import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.CANCEL;

@Service
@RequiredArgsConstructor
public class IamportServiceSubMethod {

    private final IamportClient client;
    private final SafekingPaymentRepository safekingPaymentRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * 결제 취소
     */
    @NotNull
    public IamportResponse<Payment> cancelPayment(String impUid, Double returnFee, SafekingPayment findSafekingPayment) throws IamportResponseException, IOException {

        double paid = findSafekingPayment.getAmount().doubleValue(); // 결제금액
        Double refundFee = paid - returnFee; // 환불 금액 = 결제금액 - 반품비용

        // 환불금액이 음수인 경우
        if(refundFee < 0) {
            throw new PaymentException(REFUND_FEE_CHECK);
        }

        CancelData cancelData = new CancelData(impUid, true, new BigDecimal(returnFee));
        cancelData.setReason(findSafekingPayment.getCancelReason());
        IamportResponse<Payment> cancelPaymentResponse = client.cancelPaymentByImpUid(cancelData); //imp_uid를 통한 전액취소
        findSafekingPayment.changeSafekingPayment(CANCEL, cancelPaymentResponse.getResponse()); // 결제 취소 내용으로 갱신

        // 접수일이 없는 경우에만 삽입
        if(findSafekingPayment.getCanceledRequestDate() == null) {
            findSafekingPayment.changeCanceledRequestDate(LocalDateTime.now()); // 결제 취소 접수일자 저장
        }

        return cancelPaymentResponse;
    }

    /**
     * 주문 취소(주문 상태 변경)
     */
    public Order cancelOrder(String merchantUid, String cancelReason) {
        Optional<Order> orderOptional = orderRepository.findOrderByMerchantUid(merchantUid);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));
        findOrder.cancel(cancelReason);

        return findOrder;
    }

    /**
     * 주문 삭제(주문과 관련된 테이블 삭제)
     */
    public void deleteOrder(String merchantUid) {
        // 주문 조회
        Optional<Order> orderOptional = orderRepository.findOrderByMerchantUid(merchantUid);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));

        // 상품 재고 증가
        findOrder.getOrderItems()
                .forEach(OrderItem::cancel);

        // 결제 삭제
        safekingPaymentRepository.delete(findOrder.getSafeKingPayment());
        // 주문 상품 삭제
        orderItemRepository.deleteAllByOrderItems(findOrder.getOrderItems());
        // 배송 삭제
        deliveryRepository.delete(findOrder.getDelivery());
        // 주문 삭제
        orderRepository.delete(findOrder);
    }

    /**
     * DB에서 결제 내역 조회
     */
    public SafekingPayment getSafekingPayment(String merchantUid) {
        Optional<SafekingPayment> safekingPaymentOptional = safekingPaymentRepository.findByMerchantUid(merchantUid);
        SafekingPayment findSafekingPayment = safekingPaymentOptional.orElseThrow(() -> new PaymentException(SAFEKING_PAYMENT_NONE));

        return findSafekingPayment;
    }
    /**
     * DB에서 주문 내역 조회
     */
    public Order getOrder(String merchantUid) {
        Optional<Order> optionalOrder = orderRepository.findOrderByMerchantUid(merchantUid);
        Order findOrder = optionalOrder.orElseThrow(() -> new OrderException(ORDER_NONE));

        return findOrder;
    }
}
