package com.example.springboot_backend.service;


import com.example.springboot_backend.client.IrisFeignClient;
import com.example.springboot_backend.dto.IrisRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class IrisService {

    @Autowired
    private IrisFeignClient client;

    public Object predict(IrisRequest request) {
        return client.predict(request);
    }
}
