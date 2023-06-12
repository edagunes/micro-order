package com.mealkit.order.service;

import com.mealkit.order.dao.OrderRepository;
import com.mealkit.order.dto.IngredientDto;
import com.mealkit.order.dto.ProductDto;
import com.mealkit.order.dto.requests.OrderRequest;
import com.mealkit.order.model.Order;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private static final String STOCK_INCREASE = "stock.increase";

    private static final String STOCK_DECREASE = "stock.decrease";

    private final OrderRepository orderRepository;

    private final RabbitTemplate rabbitTemplate;

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderService.class);

    public Iterable<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    public void placeOrder(OrderRequest orderRequest) throws Exception {

        final List<ProductDto> productList = orderRequest.getProductDtoList();

        final HashMap<Long, Integer> totalIngredients = new HashMap<>();

        calculateTotalIngredients(productList, totalIngredients);

        validateIngredients(totalIngredients);

        final Order order = new Order();

        assert order != null;
        if (!Objects.equals(order.getStatus(), "True")){
            throw new Exception("Error");
        }

        Integer totalPrice = 0;
        final List<Long> productIdList = new ArrayList<>();
        for (ProductDto productDto: productList){
            productIdList.add(productDto.getId());
            totalPrice += productDto.getCount() * productDto.getPrice();
        }

        order.setProductIds(productIdList);
        order.setCreateDate(LocalDate.now());
        order.setTotalPrice(totalPrice);

        orderRepository.save(order);
        LOGGER.info(String.format("Json message sent -> %s", order));
        LOGGER.info("Successful");
    }

    private void calculateTotalIngredients(List<ProductDto> productList, HashMap<Long, Integer> totalIngredients) {
        for (ProductDto productDto: productList){
            final List<IngredientDto> ingredientDtoList = productDto.getIngredientList();
            for (IngredientDto ingredientDto: ingredientDtoList){
                final Long id = ingredientDto.getId();
                final Integer count = ingredientDto.getCount();
                if (!totalIngredients.containsKey(id)){
                    totalIngredients.put(id, count);
                }
                else{
                    totalIngredients.put(id, totalIngredients.get(id) + count);
                }
            }
        }
    }

    private void validateIngredients(HashMap<Long, Integer> totalIngredients) {
        rabbitTemplate.convertAndSend(STOCK_INCREASE, totalIngredients);
    }

    public Order getOneOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElse(null);
    }

    public void deleteOneOrderById(Long orderId) {
        orderRepository.deleteById(orderId);
    }
}
