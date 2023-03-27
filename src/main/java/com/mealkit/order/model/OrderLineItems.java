package com.mealkit.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_line_items")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderLineItems {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private BigDecimal price;
    private Integer quantity;

/*    @ManyToOne
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    Order order;*/

/*    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name = "product_id")
    Product product;*/

}
