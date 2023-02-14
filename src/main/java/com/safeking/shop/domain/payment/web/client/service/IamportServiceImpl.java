package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.exception.PaymentException;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.payment.domain.entity.PaymentStatus;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentAuthCancelRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentCallbackRequest;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentWebhookRequest;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCancelPaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.PaymentCancelResponse;
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

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.LinkedHashMap;
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
    private final IamportServiceSubMethod iamportServiceSubMethod;

    /**
     * 결제 인증 취소
     */
    @Transactional
    @Override
    public String authCancel(PaymentAuthCancelRequest request) {

        Boolean success = request.getSuccess();

        // 결제 성공 여부
        if(!success) {
            // 주문 삭제
            iamportServiceSubMethod.deleteOrder(request.getMerchantUid());
            return PAYMENT_REQUEST_CANCEL;
        }
        throw new PaymentException(PAYMENT_REQUEST_CANCEL_FAIL+", success="+success);
    }

    /**
     * 결제(콜백 방식)
     */
    @Transactional
    @Override
    public PaymentResponse<PaymentCallbackResponse> paymentByCallback(PaymentCallbackRequest request) {

        // 결제 성공 여부
        if(!request.getSuccess()) {
            // 주문 삭제
            iamportServiceSubMethod.deleteOrder(request.getMerchantUid());

            // 응답 생성
            PaymentCallbackResponse paymentCallbackResponse = PaymentCallbackResponse.builder()
                    .errorMsg(request.getErrorMsg())
                    .build();

            return new PaymentResponse<>(PAYMENT_PAID_FAIL, paymentCallbackResponse);
        }

        PaymentCallbackResponse paymentCallbackResponse = null;

        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = client.paymentByImpUid(request.getImpUid());
            Payment response = iamportResponse.getResponse();

            // DB에서 결제 내역 조회
            SafekingPayment findSafekingPayment = iamportServiceSubMethod.getSafekingPayment(request.getMerchantUid());

            // 결제 금액 비교(결제 금액이 다르다면)
            if(findSafekingPayment.getAmount() != response.getAmount().intValue()) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), PAYMENT_AMOUNT_DIFFERENT_CALLBACK, 0d, findSafekingPayment);
                log.debug("[결제검증 위조] {}", PAYMENT_AMOUNT_DIFFERENT_CALLBACK);

                // 응답 생성
                return new PaymentResponse<>(PAYMENT_AMOUNT_DIFFERENT_CALLBACK, getPaymentCallbackResponse(response));
            }

            // 결제 완료
            if(response.getStatus().equals("paid")) {
                // 결제 상태 변경(결제 완료)
                findSafekingPayment.changeSafekingPayment(PAID, response);

                // 주문 상태 변경(주문 완료)
                Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
                findOrder.changeOrderStatus(COMPLETE);
                findOrder.changeSafekingPayment(findSafekingPayment);

                // 응답 생성
                paymentCallbackResponse = getPaymentCallbackResponse(response);
            }

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] {}", e.getMessage());
            throw new PaymentException(IAMPORT_PAYMENT_ERROR+", "+e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] {}", e.getMessage());
            throw new PaymentException(e.getMessage());
        }

        return new PaymentResponse<>(PAYMENT_PAID_SUCCESS, paymentCallbackResponse);
    }

    /**
     * 결제(콜백) 응답 데이터
     */
    private PaymentCallbackResponse getPaymentCallbackResponse(Payment response) {

        PaymentCallbackResponse paymentCallbackResponse = PaymentCallbackResponse.builder()
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

        return paymentCallbackResponse;
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
            SafekingPayment findSafekingPayment = iamportServiceSubMethod.getSafekingPayment(request.getMerchantUid());

            // 결제 금액 비교(결제 금액이 다르다면)
            if(findSafekingPayment.getAmount() != response.getAmount().intValue()) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), response.getCancelReason(), 0d, findSafekingPayment);

                log.debug("[결제검증 위조] {}", PAYMENT_AMOUNT_DIFFERENT_WEBHOOK);
                return;
            }

            /**
             * 결제상태.
             * ready: 미결제,
             * paid: 결제완료,
             * cancelled: 결제취소,
             * failed: 결제실패 = ['ready', 'paid', 'cancelled', 'failed'],
             */
            // 결제 완료
            if(response.getStatus().equals("paid")) {
                // 결제 상태 변경(결제 완료)
                findSafekingPayment.changeSafekingPayment(PAID, response);

                // 주문 상태 변경(주문 완료)
                Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
                findOrder.changeOrderStatus(OrderStatus.COMPLETE);
                findOrder.changeSafekingPayment(findSafekingPayment);
            }
            // 결제 취소
            else if(response.getStatus().equals("cancelled")) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), response.getCancelReason(), 0d, findSafekingPayment);
            }
            // 미결제
            else if (response.getStatus().equals("ready")) {
                // 결제 상태 변경(미결제)
                findSafekingPayment.changeSafekingPayment(READY, response);

                // 주문 상태 변경(주문대기)
                Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
                findOrder.changeOrderStatus(OrderStatus.READY);
                findOrder.changeSafekingPayment(findSafekingPayment);
            }
            // 결제 실패
            else if (response.getStatus().equals("failed")) {
                // 결제 상태 변경(결제 실패)
                findSafekingPayment.changeSafekingPayment(FAILED, response);

                // 주문 상태 변경(주문 취소)
                Order findOrder = iamportServiceSubMethod.getOrder(request.getMerchantUid());
                findOrder.changeOrderStatus(OrderStatus.CANCEL);
                findOrder.changeOrderCancelReason(response.getCancelReason());
                findOrder.changeSafekingPayment(findSafekingPayment);
            }

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] {}", e.getMessage());
            throw new PaymentException(IAMPORT_PAYMENT_ERROR+", "+e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] {}", e.getMessage());
            throw new PaymentException(e.getMessage());
        }
    }


    /**
     * 결제, 주문 취소
     */
    @Transactional
    @Override
    public PaymentCancelResponse cancel(String impUid, String merchantUid, String cancelReason, Double returnFee, SafekingPayment findSafekingPayment) {
        try {
            // 주문 취소
            Order findOrder = iamportServiceSubMethod.cancelOrder(merchantUid, cancelReason);

            // 배송 취소
            Delivery findDelivery = iamportServiceSubMethod.cancelDelivery(findOrder.getDelivery().getId());

            // 결제 취소
            IamportResponse<Payment> cancelPaymentResponse = iamportServiceSubMethod.cancelPayment(impUid, returnFee, cancelReason, findSafekingPayment);

            findOrder.changeSafekingPayment(findSafekingPayment); // 연관관계 적용
            findOrder.changeDelivery(findDelivery); // 연관관계 적용

            return getPaymentCancelResponse(cancelPaymentResponse, findSafekingPayment.getBuyerName());

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] {}", e.getMessage());
            throw new PaymentException(e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] {}", e.getMessage());
            throw new PaymentException(e.getMessage());
        }
    }

    private PaymentCancelResponse getPaymentCancelResponse(IamportResponse<Payment> cancelPaymentResponse, String buyerName) {
        PaymentCancelPaymentResponse payment = PaymentCancelPaymentResponse.builder()
                .amount(cancelPaymentResponse.getResponse().getCancelAmount().doubleValue())
                .buyerName(buyerName)
                .build();

        return PaymentCancelResponse.builder()
                .message(PAYMENT_PAID_CANCEL_SUCCESS)
                .payment(payment)
                .build();
    }

    /**
     * DB에서 결제 내역 조회
     */
    @Override
    public SafekingPayment getSafekingPayment(String merchantUid) {
        return iamportServiceSubMethod.getSafekingPayment(merchantUid);
    }
}
