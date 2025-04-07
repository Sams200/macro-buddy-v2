package com.example.sams.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class HelloWorldController {

    @GetMapping("/hello-world")
    public String getHelloMessage() {
        return "Hello World!";
    }
}
