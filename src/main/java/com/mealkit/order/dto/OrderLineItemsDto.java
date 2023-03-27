package com.mealkit.order.dto;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    private BigDecimal price;
    private Integer quantity;
}
