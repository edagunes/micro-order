package com.mealkit.order.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    User user;*/

/*    @OneToMany(mappedBy = "order")*/
/*    Set<OrderAmount> amounts;*/
    private String status;

    private Integer totalPrice;
    private LocalDate createDate;
    @ElementCollection
    @CollectionTable(
            name = "PRODUCT_IDS",
            joinColumns = @JoinColumn(
                    name = "ORDER_ID",
                    referencedColumnName = "ID"
            )
    )
    @Column(name = "PRODUCT_IDS")
    List<Long> productIds;

}