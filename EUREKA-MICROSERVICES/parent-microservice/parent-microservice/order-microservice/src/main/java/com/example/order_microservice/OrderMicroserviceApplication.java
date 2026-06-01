package com.example.order_microservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class OrderMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(OrderMicroserviceApplication.class, args);
	}

}
