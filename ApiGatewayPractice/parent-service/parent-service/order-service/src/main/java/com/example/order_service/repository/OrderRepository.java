package com.example.order_service.repository;

import com.example.order_service.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * findByUserId — Spring Data JPA parses the method name:
     * findBy → SELECT *
     * UserId → WHERE user_id = ?
     *
     * Returns all orders placed by a given user.
     * The order-service OWNS this data, so no cross-service call needed here.
     */
    List<Order> findByUserId(Long userId);
}