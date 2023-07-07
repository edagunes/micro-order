package com.mealkit.order.dto.responses;

import lombok.Data;

import java.util.Date;

@Data
public class OrderResponse {

    Long id;
    Long userId;
    String status;
    Integer totalPrice;
    Date createDate;

}
