package com.example.springboot_backend.controller;


import com.example.springboot_backend.dto.IrisRequest;
import com.example.springboot_backend.service.IrisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/iris")
@CrossOrigin("*")
public class IrisController {

    @Autowired
    private IrisService service;

    @PostMapping("/predict")
    public Object predict(@RequestBody IrisRequest request) {
        return service.predict(request);
    }
}
