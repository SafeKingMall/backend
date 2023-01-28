package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.exception.PaymentException;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentCallbackRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCallbackResponse;
import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Optional;

import static com.safeking.shop.domain.order.constant.OrderConst.*;
import static com.safeking.shop.domain.order.domain.entity.status.OrderStatus.COMPLETE;
import static com.safeking.shop.domain.payment.constant.SafeKingPaymentConst.*;
import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IamportServiceImpl implements IamportService {
    private final IamportClient client;
    private final SafekingPaymentRepository safekingPaymentRepository;
    private final OrderRepository orderRepository;

    /**
     * 결제(콜백 방식)
     */
    @Transactional
    @Override
    public PaymentResponse<PaymentCallbackResponse> paymentByCallback(PaymentCallbackRequest request) {

        // 결제 성공 여부
        if(!request.getSuccess()) {
            // 주문 취소
            cancelOrder(request.getMerchantUid(), PAYMENT_PAID_CANCEL);
            throw new PaymentException(PAYMENT_PAID_CANCEL);
        }

        PaymentCallbackResponse paymentCallbackResponse = null;

        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = client.paymentByImpUid(request.getImpUid());
            Payment response = iamportResponse.getResponse();

            // DB에서 결제 내역 조회
            SafekingPayment findSafekingPayment = getSafekingPayment(request.getMerchantUid());

            // 결제 금액 비교(결제 금액이 다르다면)
            if(findSafekingPayment.getAmount() != response.getAmount().intValue()) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), PAYMENT_AMOUNT_ERROR_CALLBACK, findSafekingPayment);

                log.debug("[PaymentException] ", PAYMENT_AMOUNT_ERROR_CALLBACK);
                throw new PaymentException(PAYMENT_AMOUNT_ERROR_CALLBACK);
            }

            // 결제 완료
            if(response.getStatus().equals("paid")) {
                // 결제 상태 변경(결제 완료)
                findSafekingPayment.changeSafekingPayment(PAID, response);

                // 주문 상태 변경(주문 완료)
                Optional<Order> optionalOrder = orderRepository.findOrderByMerchantUid(request.getMerchantUid());
                Order findOrder = optionalOrder.orElseThrow(() -> new OrderException(ORDER_NONE));
                findOrder.changeOrderStatus(COMPLETE);
                findOrder.changeSafekingPayment(findSafekingPayment);

                paymentCallbackResponse = PaymentCallbackResponse.builder()
                        .payMethod(response.getPayMethod())
                        .buyerEmail(response.getBuyerEmail())
                        .name(response.getName())
                        .buyerName(response.getBuyerName())
                        .buyerPostcode(response.getBuyerPostcode())
                        .buyerAddr(response.getBuyerAddr())
                        .buyerTel(response.getBuyerTel())
                        .amount(response.getAmount().intValue())
                        .merchantUid(response.getMerchantUid())
                        .build();
            }

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] ", e.getMessage());
            throw new PaymentException(IAMPORT_PAYMENT_ERROR+", "+e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        }

        return new PaymentResponse<>(PAYMENT_PAID_SUCCESS, paymentCallbackResponse);
    }

    /**
     * 결제(웹훅 방식)
     * 웹훅의 목적은 가맹점(DB)과 동기화
     */
    @Transactional
    @Override
    public void paymentByWebhook(PaymentWebhookRequest request) {

        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = client.paymentByImpUid(request.getImpUid());
            Payment response = iamportResponse.getResponse();

            // DB에서 결제 내역 조회
            SafekingPayment findSafekingPayment = getSafekingPayment(request.getMerchantUid());

            // 결제 금액 비교(결제 금액이 다르다면)
            if(findSafekingPayment.getAmount() != response.getAmount().intValue()) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), response.getCancelReason(), findSafekingPayment);

                log.debug("[PaymentException] ", PAYMENT_AMOUNT_ERROR_WEBHOOK);
                throw new PaymentException(PAYMENT_AMOUNT_ERROR_WEBHOOK);
            }

            // 결제 완료
            if(response.getStatus().equals("paid")) {
                // 결제 상태 변경(결제 완료)
                findSafekingPayment.changeSafekingPayment(PAID, response);

                // 주문 상태 변경(주문 완료)
                Optional<Order> optionalOrder = orderRepository.findOrderByMerchantUid(request.getMerchantUid());
                Order findOrder = optionalOrder.orElseThrow(() -> new OrderException(ORDER_NONE));
                findOrder.changeOrderStatus(COMPLETE);
                findOrder.changeSafekingPayment(findSafekingPayment);
            }

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] ", e.getMessage());
            throw new PaymentException(IAMPORT_PAYMENT_ERROR+", "+e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        }
    }

    /**
     * DB에서 결제 내역 조회
     */
    @Override
    public SafekingPayment getSafekingPayment(String merchantUid) {
        Optional<SafekingPayment> safekingPaymentOptional = safekingPaymentRepository.findByMerchantUid(merchantUid);
        SafekingPayment findSafekingPayment = safekingPaymentOptional.orElseThrow(() -> new PaymentException(SAFEKING_PAYMENT_NONE));

        return findSafekingPayment;
    }

    /**
     * 결제, 주문 취소
     */
    @Transactional
    @Override
    public IamportResponse<Payment> cancel(String impUid, String merchantUid, String cancelReason, SafekingPayment findSafekingPayment) {
        try {
            // 결제 취소
            IamportResponse<Payment> cancelPaymentResponse = cancelPayment(impUid, findSafekingPayment);

            // 주문 취소
            Order findOrder = cancelOrder(merchantUid, cancelReason);
            findOrder.changeSafekingPayment(findSafekingPayment); // 연관관계 적용

            return cancelPaymentResponse;

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        }
    }

    /**
     * 결제 취소
     */
    @NotNull
    private IamportResponse<Payment> cancelPayment(String impUid, SafekingPayment findSafekingPayment) throws IamportResponseException, IOException {
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> cancelPaymentResponse = client.cancelPaymentByImpUid(cancelData); //imp_uid를 통한 전액취소
        findSafekingPayment.changeSafekingPayment(CANCEL, cancelPaymentResponse.getResponse());

        return cancelPaymentResponse;
    }

    /**
     * 주문 취소
     */
    private Order cancelOrder(String merchantUid, String cancelReason) {
        Optional<Order> orderOptional = orderRepository.findOrderByMerchantUid(merchantUid);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));
        findOrder.cancel(cancelReason);

        return findOrder;
    }
}
