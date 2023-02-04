package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.ItemException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.item.domain.repository.ItemRepository;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
import com.safeking.shop.domain.payment.domain.entity.SafekingPayment;
import com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus;
import com.safeking.shop.domain.order.domain.repository.DeliveryRepository;
import com.safeking.shop.domain.order.domain.repository.OrderItemRepository;
import com.safeking.shop.domain.payment.domain.repository.SafekingPaymentRepository;
import com.safeking.shop.domain.order.constant.OrderConst;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderRequest;
import com.safeking.shop.domain.order.web.dto.request.user.order.OrderItemRequest;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections.ListUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceSubMethod {

    private final DeliveryRepository deliveryRepository;
    private final OrderItemRepository orderItemRepository;
    private final ItemRepository itemRepository;
    private final SafekingPaymentRepository safekingPaymentRepository;

    // 재고부족이 발생한 상품 이름을 담기 위한 list - 동기화, 동시성 이슈 우려
    private ThreadLocal<List<String>> itemNameByStockIssueHolder = new ThreadLocal<>();

    /**
     * 배송 정보 생성 및 저장
     */
    public Delivery createDelivery(OrderRequest orderRequest) {
        //배송 정보 생성
        Delivery delivery = Delivery.createDelivery(orderRequest.getReceiver(), orderRequest.getPhoneNumber(),
                orderRequest.getAddress(), DeliveryStatus.PREPARATION, orderRequest.getDeliveryMemo(), orderRequest.getEmail());
        //배송 정보 저장
        deliveryRepository.save(delivery);

        return delivery;
    }

    /**
     * 주문상품 생성
     */
    public List<OrderItem> createOrderItems(OrderRequest orderRequest, List<Item> items) {

        List<OrderItem> orderItems = new ArrayList<>();
        List<String> itemName = itemNameByStockIssueHolder.get();

        //Client에서 받은 items(orderRequest.getItemDtos())와 DB에서 조회한 items의 크기가 일치한지 확인
        if(items.size() != orderRequest.getOrderItemRequests().size()) {
            throw new ItemException(OrderConst.ORDER_ITEM_NONE);
        }

        for(int i = 0; i < items.size(); i++) {
            //주문상품 생성
            OrderItem orderItem = OrderItem.createOrderItem(
                    items.get(i),
                    items.get(i).getPrice(),
                    orderRequest.getOrderItemRequests().get(i).getCount(),
                    itemName);
            //주문상품 저장
            orderItems.add(orderItem);
        }

        // 상품 재고 예외처리(여러 상품 한번에)
        if(!CollectionUtils.isEmpty(itemName)) {
            StringBuilder name = new StringBuilder();
            for(String str : itemName) {
                name.append(str);
                name.append(", ");
            }
            itemNameByStockIssueHolder.remove();
            throw new ItemException(name.substring(0, name.length()-2)+"의 상품 재고가 부족합니다.");
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
    public SafekingPayment createPayment(List<OrderItem> orderItems, String merchantUid) {
        SafekingPayment safeKingPayment = SafekingPayment.createPayment(orderItems, merchantUid);
        safekingPaymentRepository.save(safeKingPayment);

        return safeKingPayment;
    }
}
