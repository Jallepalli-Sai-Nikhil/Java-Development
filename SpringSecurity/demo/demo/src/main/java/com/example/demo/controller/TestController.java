package com.example.demo.controller;


import com.example.demo.entity.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {

    @GetMapping("/user")
    public String getUser(){
        return "Hello, User!";
    }

    @GetMapping("/admin")
    public String getAdmin(){
        return "Hello, Admin!";
    }

}
