package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.web.dto.response.admin.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.admin.search.*;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderOrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.user.search.*;
import com.safeking.shop.domain.order.web.query.repository.querydto.AdminOrderListOrderItemQueryDto;
import com.safeking.shop.domain.order.web.query.repository.querydto.AdminOrderListQueryDto;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoDeliveryRequest;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoPaymentRequest;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.payment.web.client.dto.request.PaymentSearchCondition;
import com.safeking.shop.domain.payment.web.client.dto.response.askcancel.*;
import com.safeking.shop.domain.payment.web.client.dto.response.canceldetail.*;
import com.safeking.shop.domain.payment.web.client.dto.response.cancellist.PaymentCancelListOrderItemsResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.cancellist.PaymentCancelListOrderResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.cancellist.PaymentCancelListPaymentResponse;
import com.safeking.shop.domain.payment.web.client.dto.response.cancellist.PaymentCancelListResponse;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus.COMPLETE;
import static com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus.IN_DELIVERY;
import static com.safeking.shop.domain.order.constant.OrderConst.*;
import static com.safeking.shop.domain.payment.constant.SafeKingPaymentConst.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceSubMethod orderServiceSubMethod;
    private final OrderItemRepository orderItemRepository;
    private final SafekingPaymentRepository paymentRepository;
    private final DeliveryRepository deliveryRepository;

    /**
     * 주문
     * 주문은 결제가 완료되어야 진행함
     */
    @Override
    public OrderResponse order(Member member, OrderRequest orderRequest) {

        // 상품 조회
        List<Item> items = orderServiceSubMethod.findItems(orderRequest.getOrderItemRequests());

        // 배송 정보 생성 및 저장
        Delivery delivery = orderServiceSubMethod.createDelivery(orderRequest);

        // 주문상품 생성 및 저장
        List<OrderItem> orderItems = orderServiceSubMethod.createOrderItems(orderRequest, items);

        // 주문 번호 생성
        String merchantUid = createMerchantUid();

        // 결제 내역 임시 저장(실제 결제가 발생했을때 결제 완료로 변경)
        SafekingPayment safeKingPayment = orderServiceSubMethod.createPayment(orderItems, merchantUid);

        // 주문 생성
        Order order = Order.createOrder(member, delivery, orderRequest.getMemo(), merchantUid, safeKingPayment, orderItems);
        orderRepository.save(order);

        return getOrderResponse(merchantUid);
    }

    public OrderResponse getOrderResponse(String merchantUid) {

        OrderOrderResponse order = OrderOrderResponse.builder()
                .merchantUid(merchantUid)
                .build();

        return OrderResponse.builder()
                .message(ORDER_SUCCESS)
                .order(order)
                .build();
    }


    /**
     * 주문(배송) 정보 조회
     */
    @Transactional(readOnly = true)
    @Override
    public Order searchOrder(Long orderId, Long memberId) {
        Optional<Order> orderOptional = orderRepository.findOrderByUser(orderId, memberId);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_FIND_FAIL));

        return findOrder;
    }

    /**
     * 주문 상세 조회(유저)
     */
    @Transactional(readOnly = true)
    @Override
    public OrderDetailResponse searchOrderDetailByUser(Long orderId, Long memberId) {
        Optional<Order> findOrderDetailOptional = orderRepository.findOrderDetailByUser(orderId, memberId);
        Order findOrder = findOrderDetailOptional.orElseThrow(() -> new OrderException(ORDER_DETAIL_FIND_FAIL));

        return getOrderDetailResponseByUser(findOrder);
    }

    private OrderDetailResponse getOrderDetailResponseByUser(Order findOrderDetail) {

        // 주문 상세 조회 응답 데이터
        List<OrderDetailOrderItemResponse> orderItems = findOrderDetail.getOrderItems().stream()
                .map(oi -> OrderDetailOrderItemResponse.builder()
                        .name(oi.getItem().getName())
                        .count(oi.getCount())
                        .price(oi.getOrderPrice())
                        .thumbnail(oi.getItem().getItemPhoto().getFileName()) // 하나의 사진만
                        .build())
                .collect(Collectors.toList());

        OrderDetailDeliveryResponse delivery = OrderDetailDeliveryResponse.builder()
                .status(findOrderDetail.getDelivery().getStatus().getDescription())
                .receiver(findOrderDetail.getDelivery().getReceiver())
                .phoneNumber(findOrderDetail.getDelivery().getPhoneNumber())
                .address(findOrderDetail.getDelivery().getAddress())
                .memo(findOrderDetail.getDelivery().getMemo())
                .cost(findOrderDetail.getDelivery().getCost())
                .invoiceNumber(findOrderDetail.getDelivery().getInvoiceNumber())
                .company(findOrderDetail.getDelivery().getCompany())
                .build();

        OrderDetailPaymentResponse payment = OrderDetailPaymentResponse.builder()
                .status(findOrderDetail.getSafeKingPayment().getStatus().getDescription())
                .cardCompany(findOrderDetail.getSafeKingPayment().getCardCode())
                .payMethod(findOrderDetail.getSafeKingPayment().getPayMethod())
                .amount(findOrderDetail.getSafeKingPayment().getAmount())
                .buyerAddr(findOrderDetail.getSafeKingPayment().getBuyerAddr())
                .buyerName(findOrderDetail.getSafeKingPayment().getBuyerName())
                .buyerTel(findOrderDetail.getSafeKingPayment().getBuyerTel())
                .cashReceiptMethod("미제공")
                .businessLicenseNumber("미제공")
                .build();

        OrderDetailOrderResponse order = OrderDetailOrderResponse.builder()
                .merchantUid(findOrderDetail.getMerchantUid())
                .memo(findOrderDetail.getMemo())
                .date(findOrderDetail.getCreateDate())
                .orderItems(orderItems)
                .payment(payment)
                .delivery(delivery)
                .build();

        return OrderDetailResponse.builder()
                .message(ORDER_DETAIL_FIND_SUCCESS)
                .order(order)
                .build();
    }

    /**
     * 주문 상세 조회(어드민)
     */
    @Transactional(readOnly = true)
    @Override
    public AdminOrderDetailResponse searchOrderDetailByAdmin(Long orderId) {
        Optional<Order> findOrderDetailOptional = orderRepository.findOrderDetailByAdmin(orderId);
        Order findOrder = findOrderDetailOptional.orElseThrow(() -> new OrderException(ORDER_DETAIL_FIND_FAIL));

        return getOrderDetailResponseByAdmin(findOrder);
    }

    private AdminOrderDetailResponse getOrderDetailResponseByAdmin(Order findOrderDetail) {

        // 주문 상세 조회 응답 데이터
        List<AdminOrderDetailOrderItemsResponse> orderItems = findOrderDetail.getOrderItems().stream()
                .map(oi -> AdminOrderDetailOrderItemsResponse.builder()
                        .id(oi.getId())
                        .name(oi.getItem().getName())
                        .count(oi.getCount())
                        .price(oi.getOrderPrice())
                        .thumbnail(oi.getItem().getItemPhoto().getFileName())
                        .build())
                .collect(Collectors.toList());

        /**
         * 추후, 결제 API에서 데이터 수집해야함.
         */
        AdminOrderDetailDeliveryResponse delivery = AdminOrderDetailDeliveryResponse.builder()
                .id(findOrderDetail.getDelivery().getId())
                .status(findOrderDetail.getDelivery().getStatus().getDescription())
                .receiver(findOrderDetail.getDelivery().getReceiver())
                .phoneNumber(findOrderDetail.getDelivery().getPhoneNumber())
                .address(findOrderDetail.getDelivery().getAddress())
                .memo(findOrderDetail.getDelivery().getMemo())
                .invoiceNumber(findOrderDetail.getDelivery().getInvoiceNumber())
                .cost(findOrderDetail.getDelivery().getCost())
                .company(findOrderDetail.getDelivery().getCompany())
                .build();

        AdminOrderDetailPaymentResponse payment = AdminOrderDetailPaymentResponse.builder()
                .status(findOrderDetail.getSafeKingPayment().getStatus().getDescription())
                .company(findOrderDetail.getSafeKingPayment().getCardCode())
                .means(findOrderDetail.getSafeKingPayment().getPayMethod())
                .price(findOrderDetail.getSafeKingPayment().getAmount())
                .impUid(findOrderDetail.getSafeKingPayment().getImpUid())
                .buyerName(findOrderDetail.getMember().getUsername()) // 가맹점 유저의 이름
                .build();

        AdminOrderDetailOrderResponse order = AdminOrderDetailOrderResponse.builder()
                .id(findOrderDetail.getId())
                .merchantUid(findOrderDetail.getMerchantUid())
                .status(findOrderDetail.getStatus().getDescription())
                .price(findOrderDetail.getSafeKingPayment().getAmount())
                .memo(findOrderDetail.getMemo())
                .date(findOrderDetail.getCreateDate())
                .adminMemo(findOrderDetail.getAdminMemo())
                .orderItems(orderItems)
                .payment(payment)
                .delivery(delivery)
                .build();

        AdminOrderDetailResponse adminOrderDetailResponse = AdminOrderDetailResponse.builder()
                .message(ADMIN_ORDER_DETAIL_FIND_SUCCESS)
                .order(order)
                .build();

        return adminOrderDetailResponse;
    }

    /**
     * 주문 다건 조회
     */
    @Transactional(readOnly = true)
    @Override
    public OrderListResponse searchOrders(Pageable pageable, OrderSearchCondition condition, Long memberId) {
        Page<Order> findOrdersPage = orderRepository.findOrdersByUser(pageable, condition, memberId);
//        if(!findOrdersPage.hasContent()) {
//            throw new OrderException(ORDER_LIST_FIND_FAIL);
//        }

        return getOrderListResponse(findOrdersPage);
    }

    private OrderListResponse getOrderListResponse(Page<Order> ordersPage) {

        List<Order> findOrders = ordersPage.getContent();
        List<OrderListOrdersResponse> orders = new ArrayList<>();

        for(Order o : findOrders) {
            OrderListPaymentResponse payment = OrderListPaymentResponse.builder()
                    .status(o.getSafeKingPayment().getStatus().getDescription())
                    .canceledDate(o.getSafeKingPayment().getCancelledAt())
                    .paidDate(o.getSafeKingPayment().getPaidAt())
                    .build();

            OrderListOrderItemResponse orderItem = OrderListOrderItemResponse.builder()
                    .id(o.getOrderItems().get(0).getItem().getId())
                    .name(o.getOrderItems().get(0).getItem().getName())
                    .build();

            OrderListDeliveryResponse delivery = OrderListDeliveryResponse.builder()
                    .status(o.getDelivery().getStatus().getDescription())
                    .build();

            OrderListOrdersResponse order = OrderListOrdersResponse.builder()
                    .id(o.getId())
                    .merchantUid(o.getMerchantUid())
                    .status(o.getStatus().getDescription())
                    .price(o.getSafeKingPayment().getAmount())
                    .date(o.getCreateDate())
                    .count(o.getOrderItems().size())
                    .orderItem(orderItem)
                    .payment(payment)
                    .delivery(delivery)
                    .build();

            orders.add(order);
        }

        return OrderListResponse.builder()
                .message(ORDER_LIST_FIND_SUCCESS)
                .orders(orders)
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .size(ordersPage.getSize())
                .build();
    }

    /**
     * 주문 취소
     */
    @Override
    public void cancel(CancelRequest cancelRequest) {
        cancelRequest.getOrders()
                .forEach(cancelOrderRequest -> {
                    Optional<Order> findOrder = orderRepository.findById(cancelOrderRequest.getId());
                    Order order = findOrder.orElseThrow(() -> new OrderException(ORDER_NONE));
                    order.cancel(cancelOrderRequest.getCancelReason());
                });
    }

    /**
     * 주문 정보 수정
     */
    @Override
    public Long modifyOrder(ModifyInfoRequest modifyInfoRequest) {

        Optional<Order> findOrderOptional = orderRepository.findById(modifyInfoRequest.getOrder().getId());
        Order findOrder = findOrderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));

        Delivery delivery = findOrder.getDelivery();

        if(delivery.getStatus().equals(COMPLETE) || delivery.getStatus().equals(IN_DELIVERY)) {
            throw new OrderException(ORDER_MODIFY_DELIVERY_DONE);
        }
        else if(findOrder.getStatus().equals(OrderStatus.CANCEL)) {
            throw new OrderException(ORDER_MODIFY_FAIL);
        }

        delivery.changeDelivery(modifyInfoRequest.getDelivery().getReceiver(),
                modifyInfoRequest.getDelivery().getPhoneNumber(),
                modifyInfoRequest.getDelivery().getAddress(),
                modifyInfoRequest.getDelivery().getMemo());

        findOrder.changeMemo(modifyInfoRequest.getOrder().getMemo());

        return findOrder.getId();
    }

    /**
     * 관리자 주문 정보 수정
     */
    @Override
    public Long modifyOrderByAdmin(AdminModifyInfoRequest modifyInfoRequest, Long orderId) {

        Optional<Order> findOrderOptional = orderRepository.findById(orderId);
        Order findOrder = findOrderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));

        //배송정보 수정
        AdminModifyInfoDeliveryRequest deliveryRequest = modifyInfoRequest.getOrder().getDelivery();
        findOrder.getDelivery().changeDeliveryByAdmin(deliveryRequest.getStatus(),
                deliveryRequest.getCost(),
                deliveryRequest.getCompany(),
                deliveryRequest.getInvoiceNumber());

        //결제정보 수정
        AdminModifyInfoPaymentRequest paymentRequest = modifyInfoRequest.getOrder().getPayment();
        findOrder.getSafeKingPayment().changePaymentStatusByAdmin(paymentRequest.getStatus());

        //주문정보 수정
        findOrder.changeAdminMemoByAdmin(modifyInfoRequest.getOrder().getAdminMemo());

        return findOrder.getId();
    }

    /**
     * 주문관리 목록 조회
     */
    @Transactional(readOnly = true)
    @Override
    public AdminOrderListResponse searchOrdersByAdmin(Pageable pageable, OrderSearchCondition condition) {
        Page<AdminOrderListQueryDto> ordersPage = orderRepository.findOrdersByAdmin(pageable, condition);

        return getAdminOrderListResponse(ordersPage);
    }

    private AdminOrderListResponse getAdminOrderListResponse(Page<AdminOrderListQueryDto> ordersPage) {

        List<AdminOrderListOrderResponse> orders = new ArrayList<>();
        List<AdminOrderListQueryDto> findOrders = ordersPage.getContent();

        for(AdminOrderListQueryDto o : findOrders) {

            AdminOrderListPaymentResponse payment = AdminOrderListPaymentResponse.builder()
                    .status(o.getPayment().getStatus())
                    .build();

            AdminOrderListMemberResponse member = AdminOrderListMemberResponse.builder()
                    .name(o.getMember().getName())
                    .build();

            AdminOrderListDeliveryResponse delivery = AdminOrderListDeliveryResponse.builder()
                    .receiver(o.getDelivery().getReceiver())
                    .status(o.getDelivery().getStatus())
                    .build();

            AdminOrderListOrderResponse order = AdminOrderListOrderResponse.builder()
                    .id(o.getId())
                    .merchantUid(o.getMerchantUid())
                    .status(o.getStatus())
                    .price(o.getPrice())
                    .date(o.getDate())
                    .orderItemCount(o.getOrderItems().size())
                    .payment(payment)
                    .member(member)
                    .delivery(delivery)
                    .orderItems(o.getOrderItems().stream()
                            .map(oi -> AdminOrderListOrderItemResponse.builder()
                                    .id(oi.getId())
                                    .name(oi.getName())
                                    .build()).collect(Collectors.toList()
                            ))
                    .build();

            if(!order.getOrderItems().isEmpty()) {
                orders.add(order);
            }
        }

        return AdminOrderListResponse.builder()
                .message(ADMIN_ORDER_LIST_FIND_SUCCESS)
                .orders(orders)
                .totalElements(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .size(ordersPage.getSize())
                .build();
    }

    /**
     * 환불 내역
     */
    @Override
    public PaymentCancelListResponse searchPaymentsByCancel(Pageable pageable, PaymentSearchCondition condition, Long memberId) {
        Page<Order> ordersPage = orderRepository.findOrdersCancelByUser(pageable, condition, memberId);
//        if(!ordersPage.hasContent()) {
//            throw new PaymentException(PAYMENT_CANCEL_LIST_FIND_FAIL);
//        }
        return getPaymentCancelListResponse(ordersPage);
    }

    private static PaymentCancelListResponse getPaymentCancelListResponse(Page<Order> ordersPage) {
        List<Order> findOrders = ordersPage.getContent();
        List<PaymentCancelListOrderResponse> orders = new ArrayList<>();

        for(Order o : findOrders) {

            PaymentCancelListOrderItemsResponse orderItem = PaymentCancelListOrderItemsResponse.builder()
                    .id(o.getOrderItems().get(0).getId())
                    .name(o.getOrderItems().get(0).getItem().getName())
                    .build();

            PaymentCancelListPaymentResponse payment = PaymentCancelListPaymentResponse.builder()
                    .canceledDate(o.getSafeKingPayment().getCancelledAt())
                    .paidDate(o.getSafeKingPayment().getPaidAt())
                    .price(o.getSafeKingPayment().getAmount())
                    .status(o.getSafeKingPayment().getStatus().getDescription())
                    .build();

            PaymentCancelListOrderResponse response = PaymentCancelListOrderResponse.builder()
                    .id(o.getId())
                    .count(o.getOrderItems().size())
                    .status(o.getStatus().getDescription())
                    .date(o.getCreateDate())
                    .merchantUid(o.getMerchantUid())
                    .orderItem(orderItem)
                    .payment(payment)
                    .build();

            orders.add(response);
        }

        PaymentCancelListResponse response = PaymentCancelListResponse.builder()
                .totalElement(ordersPage.getTotalElements())
                .totalPages(ordersPage.getTotalPages())
                .size(ordersPage.getSize())
                .message(PAYMENT_CANCEL_LIST_FIND_SUCCESS)
                .order(orders)
                .build();

        return response;
    }

    @Override
    public void delete(Member member) {
        orderRepository
                .findByMember(member)
                .stream()
                .forEach(order -> orderRepository.delete(order));
    }

    @Override
    public void deleteByMemberBatch(Member member) {
        List<Order> orderList = orderRepository.findByMember(member);

        List<Delivery> deliveryList =
                orderList.stream()
                .map(order -> order.getDelivery())
                .collect(Collectors.toList());

        List<SafekingPayment> safekingPaymentList =
                orderList.stream()
                .map(order -> order.getSafeKingPayment())
                .collect(Collectors.toList());

        List<OrderItem> orderItemList = orderItemRepository.findByOrderList(orderList);

        // orderItem delete
        orderItemRepository.deleteByOrderBatch(orderItemList);
        // order delete
        orderRepository.deleteAllByMemberBatch(orderList);
        // delivery delete
        deliveryRepository.deleteByOrderBatch(deliveryList);
        // payments delete
        paymentRepository.deleteByOrderBatch(safekingPaymentList);
    }

    /**
     * 환불신청 단건 조회
     */
    @Override
    public PaymentAskCancelResponse searchPaymentByCancel(Long orderId, Long memberId) {
        Optional<Order> orderOptional = orderRepository.findOrderAskPaymentCancelByUser(orderId, memberId);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(PAYMENT_CANCEL_FIND_FAIL));

        return getPaymentAskCancelResponse(findOrder);
    }

    private PaymentAskCancelResponse getPaymentAskCancelResponse(Order findOrder) {
        // 주문 상품
        List<PaymentAskCancelOrderItemResponse> orderItem = findOrder.getOrderItems().stream()
                .map(oi -> PaymentAskCancelOrderItemResponse.builder()
                        .count(oi.getCount())
                        .price(oi.getItem().getPrice())
                        .name(oi.getItem().getName())
                        .thumbnail(oi.getItem().getItemPhoto().getFileName())
                        .build())
                .collect(Collectors.toList());

        // 배송
        PaymentAskCancelDeliveryResponse delivery = PaymentAskCancelDeliveryResponse.builder()
                .status(findOrder.getDelivery().getStatus().getDescription())
                .build();

        // 결제
        PaymentAskCancelPaymentResponse payment = PaymentAskCancelPaymentResponse.builder()
                .price(findOrder.getSafeKingPayment().getAmount())
                .buyerName(findOrder.getSafeKingPayment().getBuyerName())
                .status(findOrder.getSafeKingPayment().getStatus().getDescription())
                .cardCompany(findOrder.getSafeKingPayment().getCardCode())
                .payMethod(findOrder.getSafeKingPayment().getPayMethod())
                .build();

        // 주문
        PaymentAskCancelOrderResponse order = PaymentAskCancelOrderResponse.builder()
                .id(findOrder.getId())
                .orderItem(orderItem)
                .date(findOrder.getCreateDate())
                .build();

        return PaymentAskCancelResponse.builder()
                .message(PAYMENT_CANCEL_FIND_SUCCESS)
                .payment(payment)
                .delivery(delivery)
                .order(order)
                .build();
    }

    /**
     * 환불 상세 내역
     */
    @Override
    public PaymentCancelDetailResponse searchPaymentCancelDetailByUser(Long orderId, Long memberId) {
        Optional<Order> orderOptional = orderRepository.findOrderPaymentCancelDetailByUser(orderId, memberId);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(PAYMENT_CANCEL_DETAIL_FIND_FAIL));

        return getPaymentCancelDetailResponse(findOrder);
    }

    private PaymentCancelDetailResponse getPaymentCancelDetailResponse(Order findOrder) {

        Integer amount = findOrder.getSafeKingPayment().getAmount(); // 결제 금액
        Integer cancelAmount = findOrder.getSafeKingPayment().getCancelAmount(); // 환불 금액
        if(cancelAmount == null || amount == null) {
            throw new OrderException("환불처리가 되지 않은 것 같습니다. 관리자에게 문의하세요.");
        }

        List<PaymentCancelDetailOrderItemResponse> orderItems = findOrder.getOrderItems().stream()
                .map(oi -> PaymentCancelDetailOrderItemResponse.builder()
                        .count(oi.getCount())
                        .price(oi.getOrderPrice())
                        .name(oi.getItem().getName())
                        .thumbnail(oi.getItem().getItemPhoto().getFileName())
                        .build())
                .collect(Collectors.toList());

        PaymentCancelDetailDeliveryResponse delivery = PaymentCancelDetailDeliveryResponse.builder()
                .status(findOrder.getDelivery().getStatus().getDescription())
                .cost(findOrder.getDelivery().getCost())
                .company(findOrder.getDelivery().getCompany())
                .invoiceNumber(findOrder.getDelivery().getInvoiceNumber())
                .build();

        PaymentCancelDetailPaymentResponse payment = PaymentCancelDetailPaymentResponse.builder()
                .cancelAmount(findOrder.getSafeKingPayment().getCancelAmount())
                .buyerAddr(findOrder.getSafeKingPayment().getBuyerAddr())
                .buyerTel(findOrder.getSafeKingPayment().getBuyerTel())
                .buyerName(findOrder.getSafeKingPayment().getBuyerName())
                .canceledDate(findOrder.getSafeKingPayment().getCancelledAt())
                .payMethod(findOrder.getSafeKingPayment().getPayMethod())
                .cardCompany(findOrder.getSafeKingPayment().getCardCode())
                .canceledRequestDate(findOrder.getSafeKingPayment().getCanceledRequestDate())
                .cancelReason(findOrder.getSafeKingPayment().getCancelReason())
                .status(findOrder.getSafeKingPayment().getStatus().getDescription())
                .refundFee(Integer.valueOf(amount - cancelAmount)) // 반품비 = 결제금액 - 환불금액
                .build();

        PaymentCancelDetailOrderResponse order = PaymentCancelDetailOrderResponse.builder()
                .orderItems(orderItems)
                .payment(payment)
                .delivery(delivery)
                .date(findOrder.getCreateDate())
                .merchantUid(findOrder.getMerchantUid())
                .build();

        return PaymentCancelDetailResponse.builder()
                .message(PAYMENT_CANCEL_DETAIL_FIND_SUCCESS)
                .order(order)
                .build();
    }

    /**
     * 주문 번호 생성 함수
     */
    private String createMerchantUid() {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        LocalDateTime localDateTime = LocalDateTime.now(); //2023-02-05T00:52:30.229273

        String time = localDateTime.toString()
                .substring(2, 19)
                .replaceAll("T", "")
                .replaceAll("-", "")
                .replaceAll(":", "")
                ;

        // e.g) SFK-2302050056-bb1b88c9
        return "SFK-"+time+"-"+uuid;
    }
}
