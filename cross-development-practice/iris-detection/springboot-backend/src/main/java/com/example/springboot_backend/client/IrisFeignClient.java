package com.example.springboot_backend.client;

import com.example.springboot_backend.dto.IrisRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient(name = "flask-client", url = "${flask.service.url}")
public interface IrisFeignClient {

    @PostMapping("/predict")
    Object predict(IrisRequest request);
}