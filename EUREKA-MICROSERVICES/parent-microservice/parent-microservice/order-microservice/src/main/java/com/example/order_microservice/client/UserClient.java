package com.example.order_microservice.client;


import com.example.order_microservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-microservice")
public interface UserClient {

    @GetMapping("/users/{id}")
    UserDto getUserById(@PathVariable Long id);
}
