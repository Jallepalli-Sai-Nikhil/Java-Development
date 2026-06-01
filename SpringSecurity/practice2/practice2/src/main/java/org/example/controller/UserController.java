package org.example.controller;


import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Repository
@RequestMapping("/user")
public class UserController {
    @GetMapping("/hello")
    public String hello(){
        return "hello";
    }
}
