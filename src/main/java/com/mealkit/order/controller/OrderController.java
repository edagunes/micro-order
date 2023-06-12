package com.mealkit.order.controller;

import com.mealkit.order.dto.requests.OrderRequest;
import com.mealkit.order.model.Order;
import com.mealkit.order.service.OrderService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public Iterable<Order> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("/{orderId}")
    public Order getOneOrder(@PathVariable Long orderId){
        return orderService.getOneOrderById(orderId);
    }

    @PostMapping
    public void placeOrder(@RequestBody OrderRequest placeOrderRequest){
        orderService.placeOrder(placeOrderRequest);
    }

    @DeleteMapping("/{orderId}")
    public void deleteOneOrder(@PathVariable Long orderId){
        orderService.deleteOneOrderById(orderId);
    }
}
