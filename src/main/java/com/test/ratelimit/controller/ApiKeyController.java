package com.test.ratelimit.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.test.ratelimit.model.PricingPlan;
import com.test.ratelimit.service.PricingPlanService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = "/api/key")
@RequiredArgsConstructor
class ApiKeyController {

    private final PricingPlanService pricingPlanService;

    @PostMapping("/{pricingPlan}")
    public String getKey(@PathVariable PricingPlan pricingPlan) {
        return pricingPlanService.createKey(pricingPlan);

    }

}
