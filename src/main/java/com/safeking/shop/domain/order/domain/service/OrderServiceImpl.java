package com.safeking.shop.domain.order.domain.service;

import com.safeking.shop.domain.exception.OrderException;
import com.safeking.shop.domain.item.domain.entity.Item;
import com.safeking.shop.domain.order.domain.entity.Delivery;
import com.safeking.shop.domain.order.domain.entity.Order;
import com.safeking.shop.domain.order.domain.entity.OrderItem;
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
import com.safeking.shop.domain.user.domain.entity.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus.COMPLETE;
import static com.safeking.shop.domain.order.domain.entity.status.DeliveryStatus.IN_DELIVERY;
import static com.safeking.shop.domain.order.constant.OrderConst.*;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderServiceSubMethod orderServiceSubMethod;

    /**
     * ??????
     * ????????? ????????? ??????????????? ?????????
     */
    @Override
    public Long order(Member member, OrderRequest orderRequest) {

        // ?????? ??????
        List<Item> items = orderServiceSubMethod.findItems(orderRequest.getOrderItemRequests());

        // ?????? ?????? ?????? ??? ??????
        Delivery delivery = orderServiceSubMethod.createDelivery(orderRequest);

        // ???????????? ?????? ??? ??????
        List<OrderItem> orderItems = orderServiceSubMethod.createOrderItems(orderRequest, items);

        // ?????? ?????? ?????? ??????(?????? ????????? ??????????????? ?????? ????????? ??????)
        SafekingPayment safeKingPayment = orderServiceSubMethod.createPayment(orderItems);

        // ?????? ??????
        Order order = Order.createOrder(member, delivery, orderRequest.getMemo(), safeKingPayment, orderItems);
        orderRepository.save(order);

        return order.getId();
    }

    /**
     * ??????(??????) ?????? ??????
     */
    @Override
    public Order searchOrder(Long id) {
        Optional<Order> orderOptional = orderRepository.findOrder(id);
        Order findOrder = orderOptional.orElseThrow(() -> new OrderException(ORDER_FIND_FAIL));

        return findOrder;
    }

    /**
     * ?????? ?????? ??????
     */
    @Override
    public Order searchOrderDetail(Long id) {
        Optional<Order> findOrderDetailOptional = orderRepository.findOrderDetail(id);
        Order findOrder = findOrderDetailOptional.orElseThrow(() -> new OrderException(ORDER_DETAIL_FIND_FAIL));

        return findOrder;
    }

    /**
     * ?????? ?????? ??????
     */
    @Override
    public Page<Order> searchOrders(Pageable pageable, OrderSearchCondition condition, Long memberId) {
        Page<Order> findOrdersPage = orderRepository.findOrders(pageable, condition, memberId);
        if(findOrdersPage.isEmpty()) {
            throw new OrderException(ORDER_LIST_FIND_FAIL);
        }

        return findOrdersPage;
    }
    /**
     * ?????? ??????
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
     * ?????? ?????? ??????
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
     * ????????? ?????? ?????? ??????
     */
    @Override
    public Long modifyOrderByAdmin(AdminModifyInfoRequest modifyInfoRequest, Long orderId) {

        Optional<Order> findOrderOptional = orderRepository.findById(orderId);
        Order findOrder = findOrderOptional.orElseThrow(() -> new OrderException(ORDER_NONE));

        //???????????? ??????
        AdminModifyInfoDeliveryRequest deliveryRequest = modifyInfoRequest.getOrder().getDelivery();
        findOrder.getDelivery().changeDeliveryByAdmin(deliveryRequest.getStatus(),
                deliveryRequest.getCost(),
                deliveryRequest.getCompany(),
                deliveryRequest.getInvoiceNumber());

        //???????????? ??????
        AdminModifyInfoPaymentRequest paymentRequest = modifyInfoRequest.getOrder().getPayment();
        findOrder.getSafeKingPayment().changePaymentStatusByAdmin(paymentRequest.getStatus());

        //???????????? ??????
        findOrder.changeAdminMemoByAdmin(modifyInfoRequest.getOrder().getAdminMemo());

        return findOrder.getId();
    }

    /**
     * ???????????? ?????? ??????
     */
    @Override
    public Page<Order> searchOrdersByAdmin(Pageable pageable, OrderSearchCondition condition) {
        return orderRepository.findOrdersByAdmin(pageable, condition);
    }
}
