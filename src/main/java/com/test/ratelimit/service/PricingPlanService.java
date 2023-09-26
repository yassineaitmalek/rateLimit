package com.test.ratelimit.service;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.test.ratelimit.model.PricingPlan;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;

@Service
public class PricingPlanService {

    public final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    public final Map<String, PricingPlan> planCache = new ConcurrentHashMap<>();

    public Bucket insertBucket(String key, Bucket bucket) {
        return cache.put(key, bucket);
    }

    public Bucket resolveBucket(String key) {
        return cache.computeIfAbsent(key, this::newBucket);
    }

    public Bucket newBucket(String apiKey) {
        PricingPlan pricingPlan = planCache.get(apiKey);
        return bucket(getLimit(pricingPlan));
    }

    public Bucket bucket(Bandwidth limit) {
        return Bucket.builder()
                .addLimit(limit)
                .build();
    }

    public Bandwidth getLimit(PricingPlan plan) {
        return Bandwidth.classic(plan.getBucketCapacity(),
                Refill.intervally(plan.getBucketCapacity(), Duration.ofMinutes(1)));
    }

    public String createKey(PricingPlan pricingPlan) {
        String key = UUID.randomUUID().toString();
        insertBucket(key, bucket(getLimit(pricingPlan)));
        planCache.put(key, pricingPlan);
        return key;

    }

    public ConsumptionProbe getConsumptionProbe(String key) {
        return resolveBucket(key)
                .tryConsumeAndReturnRemaining(1);
    }
}
