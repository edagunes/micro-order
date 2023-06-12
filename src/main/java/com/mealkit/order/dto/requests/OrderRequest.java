package com.mealkit.order.dto.requests;

import com.mealkit.order.dto.OrderLineItemsDto;
import com.mealkit.order.dto.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {
    private List<ProductDto> productDtoList;
}
