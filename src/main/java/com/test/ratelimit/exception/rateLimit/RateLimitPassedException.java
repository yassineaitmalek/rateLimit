package com.test.ratelimit.exception.rateLimit;

import com.test.ratelimit.exception.config.ApiException;

public class RateLimitPassedException extends ApiException {

  public RateLimitPassedException(long waitForRefill) {
    super("You have exhausted your API Request Quota, waitForRefill : " + waitForRefill + "s");

  }

}
