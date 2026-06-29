package com.placify.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "🚀 Placify Backend is Running!";
    }

    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}