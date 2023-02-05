package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.web.dto.response.admin.orderdetail.*;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderOrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.order.OrderResponse;
import com.safeking.shop.domain.order.web.dto.response.user.orderdetail.*;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.order.domain.entity.status.OrderStatus;
import com.safeking.shop.domain.order.domain.repository.OrderRepository;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoDeliveryRequest;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoPaymentRequest;
import com.safeking.shop.domain.order.web.dto.request.admin.modify.AdminModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelRequest;
import com.safeking.shop.domain.order.web.dto.request.user.cancel.CancelOrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.modify.ModifyInfoRequest;
import com.safeking.shop.domain.order.web.dto.request.user.search.OrderSearchCondition;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus.COMPLETE;
import static com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus.IN_DELIVERY;
import static com.safeking.shop.domain.order.constant.OrderConst.*;

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
    public Order searchOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findOrder(id);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_FIND_FAIL));

        return findOrder;
    }

    /**
     * 주문 상세 조회(유저)
     */
    @Transactional(readOnly = true)
    @Override
    public OrderDetailResponse searchOrderDetailByUser(Long id) {
        Optional<Order> findOrderDetailOptional = orderRepository.findOrderDetail(id);
        Order findOrder = findOrderDetailOptional.orElseThrow(() -> new OrderException(ORDER_DETAIL_FIND_FAIL));

        return getOrderDetailResponseByUser(findOrder);
    }

    private OrderDetailResponse getOrderDetailResponseByUser(Order findOrderDetail) {

        // 주문 상세 조회 응답 데이터
        List<OrderDetailOrderItemResponse> orderItems = findOrderDetail.getOrderItems().stream()
                .map(oi -> OrderDetailOrderItemResponse.builder()
                        .id(oi.getId())
                        .name(oi.getItem().getName())
                        .count(oi.getCount())
                        .price(oi.getOrderPrice())
                        .thumbnail(oi.getItem().getItemPhotos().get(0).getFileName()) // 하나의 사진만
                        .build())
                .collect(Collectors.toList());

        OrderDetailDeliveryResponse delivery = OrderDetailDeliveryResponse.builder()
                .id(findOrderDetail.getDelivery().getId())
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
                .impUid(findOrderDetail.getSafeKingPayment().getImpUid())
                .cancelReason(findOrderDetail.getSafeKingPayment().getCancelReason())
                .canceledRequestDate(findOrderDetail.getSafeKingPayment().getCanceledRequestDate())
                .canceledDate(findOrderDetail.getSafeKingPayment().getCancelledAt())
                .paidDate(findOrderDetail.getSafeKingPayment().getPaidAt())
                .failedDate(findOrderDetail.getSafeKingPayment().getFailedAt())
                .cardCompany(findOrderDetail.getSafeKingPayment().getCardCode())
                .payMethod(findOrderDetail.getSafeKingPayment().getPayMethod())
                .build();

        OrderDetailOrderResponse order = OrderDetailOrderResponse.builder()
                .id(findOrderDetail.getId())
                .merchantUid(findOrderDetail.getMerchantUid())
                .status(findOrderDetail.getStatus().getDescription())
                .price(findOrderDetail.getSafeKingPayment().getAmount())
                .memo(findOrderDetail.getMemo())
                .date(findOrderDetail.getCreateDate())
                .orderItems(orderItems)
                .payment(payment)
                .delivery(delivery)
                .cancelReason(findOrderDetail.getCancelReason())
                .build();

        OrderDetailMember member = OrderDetailMember.builder()
                .name(findOrderDetail.getMember().getName())
                .phoneNumber(findOrderDetail.getMember().getPhoneNumber())
                .build();

        return OrderDetailResponse.builder()
                .message(ORDER_DETAIL_FIND_SUCCESS)
                .order(order)
                .member(member)
                .build();
    }

    /**
     * 주문 상세 조회(어드민)
     */
    @Transactional(readOnly = true)
    @Override
    public AdminOrderDetailResponse searchOrderDetailByAdmin(Long id) {
        Optional<Order> findOrderDetailOptional = orderRepository.findOrderDetail(id);
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

        /**
         * 추후, 결제 API에서 데이터 수집해야함.
         */
        AdminOrderDetailPaymentResponse payment = AdminOrderDetailPaymentResponse.builder()
                .status(findOrderDetail.getSafeKingPayment().getStatus().getDescription())
                .company(findOrderDetail.getSafeKingPayment().getCardCode())
                .means(findOrderDetail.getSafeKingPayment().getPayMethod())
                .price(findOrderDetail.getSafeKingPayment().getAmount())
                .impUid(findOrderDetail.getSafeKingPayment().getImpUid())
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
    public Page<Order> searchOrders(Pageable pageable, OrderSearchCondition condition, Long memberId) {
        Page<Order> findOrdersPage = orderRepository.findOrders(pageable, condition, memberId);
        if(findOrdersPage.isEmpty()) {
            throw new OrderException(ORDER_LIST_FIND_FAIL);
        }

        return findOrdersPage;
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
    public Page<Order> searchOrdersByAdmin(Pageable pageable, OrderSearchCondition condition) {
        return orderRepository.findOrdersByAdmin(pageable, condition);
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
