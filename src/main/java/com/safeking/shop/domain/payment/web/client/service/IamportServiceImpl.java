package com.safeking.shop.domain.payment.web.client.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.exception.PaymentException;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.payment.domain.entity.PaymentStatus;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentRequest;
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
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;

import static com.safeking.shop.domain.order.constant.OrderConst.*;
import static com.safeking.shop.domain.payment.constant.SafeKingPaymentConst.*;
import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.*;
import static com.safeking.shop.domain.payment.domain.entity.PaymentStatus.PAID;

@Slf4j
@Service
@RequiredArgsConstructor
public class IamportServiceImpl implements IamportService {
    private final IamportClient client;
    private final SafekingPaymentRepository safekingPaymentRepository;
    private final OrderRepository orderRepository;

    /**
     * 결제(콜백 방식)
     */
    @Override
    public PaymentResponse<PaymentCallbackResponse> paymentByCallback(PaymentRequest request) {

        // 결제 성공 여부
        if(!request.getSuccess()) {
            throw new PaymentException(PAYMENT_PAID_FAILED);
        }

        PaymentCallbackResponse paymentCallbackResponse = null;

        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = client.paymentByImpUid(request.getImpUid());
            Payment response = iamportResponse.getResponse();

            // DB에서 결제 내역 조회
            Optional<SafekingPayment> safekingPaymentOptional = safekingPaymentRepository.findByMerchantUid(request.getMerchantUid());
            SafekingPayment findSafekingPayment = safekingPaymentOptional.orElseThrow(() -> new PaymentException(SAFEKING_PAYMENT_NONE));

            // 결제 금액 비교(결제 금액이 다르다면)
            if(findSafekingPayment.getAmount() != response.getAmount().intValue()) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), response.getCancelReason(), findSafekingPayment);
            }

            // 결제 완료
            if (response.getStatus().equals("paid")) {
                findSafekingPayment.changeSafekingPayment(PAID, response);

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
            throw new PaymentException(e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        }

        return new PaymentResponse<>(PAYMENT_PAID_SUCCESS, paymentCallbackResponse);
    }

    /**
     * 결제(웹훅 방식)
     */
    @Override
    public void paymentByWebhook(PaymentWebhookRequest request) {

        try {
            // 결제 단건 조회
            IamportResponse<Payment> iamportResponse = client.paymentByImpUid(request.getImpUid());
            Payment response = iamportResponse.getResponse();

            // DB에서 결제 내역 조회
            Optional<SafekingPayment> safekingPaymentOptional = safekingPaymentRepository.findByMerchantUid(request.getMerchantUid());
            SafekingPayment findSafekingPayment = safekingPaymentOptional.orElseThrow(() -> new PaymentException(SAFEKING_PAYMENT_NONE));

            // 결제 금액 비교(결제 금액이 다르다면)
            if(findSafekingPayment.getAmount() != response.getAmount().intValue()) {
                // 결제, 주문 취소 로직
                cancel(request.getImpUid(), response.getMerchantUid(), response.getCancelReason(), findSafekingPayment);
            }

            // 결제 완료
            else if(response.getStatus().equals("paid")) {
                findSafekingPayment.changeSafekingPayment(PAID, response);
            }

        } catch (IamportResponseException e) {
            log.error("[IamportResponseException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        } catch (IOException e) {
            log.error("[IOException] ", e.getMessage());
            throw new PaymentException(e.getMessage());
        }
    }

    /**
     * 결제, 주문 취소
     */
    private void cancel(String impUid, String merchantUid, String cancelReason, SafekingPayment findSafekingPayment) throws IamportResponseException, IOException {
        // 결제 취소
        CancelData cancelData = new CancelData(impUid, true);
        IamportResponse<Payment> cancelPaymentResponse = client.cancelPaymentByImpUid(cancelData); //imp_uid를 통한 전액취소
        findSafekingPayment.changeSafekingPayment(CANCELLED, cancelPaymentResponse.getResponse());

        // 주문 취소
        Optional<Order> orderOptional = orderRepository.findOrderByMerchantUid(merchantUid);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_CANCEL_FAIL));
        findOrder.cancel(cancelReason);
        findOrder.changeSafekingPayment(findSafekingPayment);

        log.error("[PaymentException] ", PAYMENT_AMOUNT_ERROR);

        throw new PaymentException(PAYMENT_AMOUNT_ERROR);
    }


}
