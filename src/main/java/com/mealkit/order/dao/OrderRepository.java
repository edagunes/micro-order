package com.mealkit.order.dao;

import com.mealkit.order.model.Order;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends CrudRepository<Order,Long> {

/*    @Query(value = "SELECT id FROM orders WHERE user_id = :user_id ORDER BY create_date DESC limit 10",
    nativeQuery = true)
    List<Long> findTopByUserId(@Param("userId") Long userId);*/
}
