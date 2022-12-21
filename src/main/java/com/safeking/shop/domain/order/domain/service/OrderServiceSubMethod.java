package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.ItemException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.order.domain.entity.Payment;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.order.domain.repository.PaymentRepository;
import com.safeking.shop.domain.order.constant.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderItemRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderServiceSubMethod {

    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final PaymentRepository paymentRepository;

    /**
     * 배송 정보 생성 및 저장
     */
    @Transactional
    public Delivery createDelivery(OrderRequest orderRequest) {
        //배송 정보 생성
        Delivery delivery = Delivery.createDelivery(orderRequest.getReceiver(), orderRequest.getPhoneNumber(),
                orderRequest.getAddress(), DeliveryStatus.PREPARATION, orderRequest.getDeliveryMemo());
        //배송 정보 저장
        deliveryRepository.save(delivery);

        return delivery;
    }

    /**
     * 주문상품 생성
     */
    @Transactional
    public List<OrderItem> createOrderItems(OrderRequest orderRequest, List<Item> items) {

        List<OrderItem> orderItems = new ArrayList<>();

        //Client에서 받은 items(orderRequest.getItemDtos())와 DB에서 조회한 items의 크기가 일치한지 확인
        if(items.size() != orderRequest.getOrderItemRequests().size()) {
            throw new ItemException(OrderConst.ORDER_ITEM_NONE);
        }

        for(int i = 0; i < items.size(); i++) {
            //주문상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(
                    items.get(i),
                    items.get(i).getPrice(),
                    orderRequest.getOrderItemRequests().get(i).getCount());
            //주문상품 저장
            orderItems.add(orderItem);
        }

        return orderItems;
    }

    /**
     * 상품 조회
     */
    public List<Item> findItems(List<OrderItemRequest> orderItemRequests) {

        Optional<Item> findItemsOptional;
        List<Item> items = new ArrayList<>();

        List<Long> itemIds = orderItemRequests.stream()
                .map(OrderItemRequest::getId)
                .collect(Collectors.toList());

        for(Long id : itemIds) {
            findItemsOptional = itemRepository.findById(id);
            items.add(findItemsOptional.orElseThrow(() -> new ItemException(OrderConst.ORDER_ITEM_NONE)));
        }

        return items;
    }

    /**
     * 결제
     */
    public Payment createPayment(List<OrderItem> orderItems, String number, String means) {
        Payment payment = Payment.createPayment(orderItems, number, means);
        paymentRepository.save(payment);

        return payment;
    }
}
