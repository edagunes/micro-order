package com.mealkit.order.service;

import com.mealkit.order.dao.OrderRepository;
import com.mealkit.order.dto.OrderLineItemsDto;
import com.mealkit.order.dto.requests.OrderRequest;
import com.mealkit.order.dto.responses.OrderResponse;
import com.mealkit.order.model.Order;
import com.mealkit.order.model.OrderLineItems;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository){
        this.orderRepository = orderRepository;
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public String placeOrder(OrderRequest orderRequest) {
        Order order = new Order();

        List<OrderLineItems> orderLineItems= orderRequest.getOrderLineItemsDtoList()
                .stream()
                .map(this::mapToDto)
                .toList();

        order.setOrderLineItemsList(orderLineItems);
        orderRepository.save(order);
        return "Order Placed Successfully";
    }

    public Order getOneOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void deleteOneOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        return orderLineItems;
    }
}
