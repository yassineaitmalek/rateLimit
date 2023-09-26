package com.test.ratelimit.config;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.test.ratelimit.exception.rateLimit.MissingApiKeyHeaderException;
import com.test.ratelimit.exception.rateLimit.RateLimitPassedException;
import com.test.ratelimit.service.PricingPlanService;

import io.github.bucket4j.ConsumptionProbe;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RateLimitInterceptor implements HandlerInterceptor {

    private static final String HEADER_API_KEY = "X-api-key";

    private static final String HEADER_LIMIT_REMAINING = "X-Rate-Limit-Remaining";

    @Autowired
    private PricingPlanService pricingPlanService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        String apiKey = request.getHeader(HEADER_API_KEY);

        if (apiKey == null || apiKey.isEmpty()) {
            log.error("empty or null key");
            throw new MissingApiKeyHeaderException();
        }

        ConsumptionProbe probe = pricingPlanService.getConsumptionProbe(apiKey);

        if (probe.isConsumed()) {
            log.info("success");
            log.info("remaining requests = " + String.valueOf(probe.getRemainingTokens()));
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            log.info("waitForRefill = " + waitForRefill);
            response.addHeader(HEADER_LIMIT_REMAINING, String.valueOf(probe.getRemainingTokens()));
            return true;

        } else {
            log.error("fail");
            long waitForRefill = probe.getNanosToWaitForRefill() / 1_000_000_000;
            log.error("waitForRefill = " + waitForRefill);

            throw new RateLimitPassedException(waitForRefill);

        }
    }
}