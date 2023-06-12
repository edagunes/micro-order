package com.mealkit.order.dto;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItemsDto {
    private Long id;
    private ProductDto product;
    private Integer quantity;
}
