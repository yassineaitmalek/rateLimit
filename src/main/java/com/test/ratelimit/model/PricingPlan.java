package com.test.ratelimit.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PricingPlan {

    FREE(1),

    BASIC(10),

    PROFESSIONAL(20);

    private Integer bucketCapacity;

}
