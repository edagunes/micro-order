package com.mealkit.order.service;

import com.mealkit.order.dao.OrderLineItemsRepository;
import com.mealkit.order.dao.OrderRepository;
import com.mealkit.order.dto.OrderLineItemsDto;
import com.mealkit.order.dto.requests.OrderRequest;
import com.mealkit.order.model.Order;
import com.mealkit.order.model.OrderLineItems;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderService {

    private static final String QUEUE_NAME = "stockUpdates";

    private final OrderRepository orderRepository;

    private final OrderLineItemsRepository orderLineItemsRepository;

    private final RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository, RabbitTemplate rabbitTemplate, OrderLineItemsRepository orderLineItemsRepository){
        this.orderRepository = orderRepository;
        this.rabbitTemplate = rabbitTemplate;
        this.orderLineItemsRepository = orderLineItemsRepository;
    }

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void placeOrder(OrderRequest orderRequest) {
        final Order order = new Order();

        final List<OrderLineItemsDto> orderLineItemsDtoList = orderRequest.getOrderLineItemsDtoList();
        final List<OrderLineItems> orderLineItemsList = orderLineItemsDtoList.stream().map(orderLineItemsDto -> {
            try {
                return mapToEntity(orderLineItemsDto);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).toList();
        Integer totalPrice = 0;
        for (OrderLineItems orderLineItems: orderLineItemsList){  //TODO Başka methoda taşı
            totalPrice += orderLineItems.getPrice();
        }
        order.setOrderLineItemsList(orderLineItemsList);
        //TODO: validate
        order.setCreateDate(LocalDate.now());
        order.setStatus("True");
        order.setTotalPrice(totalPrice);
        orderRepository.save(order);
        LOGGER.info(String.format("Json message sent -> %s", order));
        rabbitTemplate.convertAndSend(QUEUE_NAME, orderLineItemsDtoList);
        LOGGER.info("Successful");
    }

    public Order getOneOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void deleteOneOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }

    private OrderLineItems mapToEntity(OrderLineItemsDto orderLineItemsDto) throws Exception {
        final Optional<OrderLineItems> orderLineItemsOptional = orderLineItemsRepository.findById(orderLineItemsDto.getId());
        if (orderLineItemsOptional.isPresent()){
            return orderLineItemsOptional.get();
        }
        throw new Exception("Error");
    }

}
