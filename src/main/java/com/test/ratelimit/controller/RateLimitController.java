package com.test.ratelimit.controller;

import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/rateLimit")
class RateLimitController {

    @GetMapping(value = "/test")
    public String test(@RequestHeader("X-api-key") String apiKey) {
        return "Hello";
    }

}
