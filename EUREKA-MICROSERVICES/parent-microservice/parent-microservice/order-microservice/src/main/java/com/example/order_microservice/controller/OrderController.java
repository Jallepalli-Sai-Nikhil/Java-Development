package com.example.order_microservice.controller;


import com.example.order_microservice.client.UserClient;
import com.example.order_microservice.dto.UserDto;
import com.example.order_microservice.entity.Order;
import com.example.order_microservice.repository.OrderRepository;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderRepository orderRepository;
    private final UserClient userClient;

    public OrderController(OrderRepository orderRepository, UserClient userClient) {
        this.orderRepository = orderRepository;
        this.userClient = userClient;
    }

    // Get orders by userId + user details
    @GetMapping("/user/{userId}")
    public Map<String, Object> getOrdersByUser(@PathVariable Long userId) {

        UserDto user = userClient.getUserById(userId);
        List<Order> orders = orderRepository.findByUserId(userId);

        Map<String, Object> response = new HashMap<>();
        response.put("user", user);
        response.put("orders", orders);

        return response;
    }
}
