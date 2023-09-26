package com.test.ratelimit.exception.rateLimit;

import com.test.ratelimit.exception.config.ApiException;

public class MissingApiKeyHeaderException extends ApiException {

  public MissingApiKeyHeaderException() {
    super("Api key is missing");

  }

}
