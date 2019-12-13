package org.goblinframework.core.web;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.OkHttp3ClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

final public class CoreRestTemplate {
  private static final Logger logger = LoggerFactory.getLogger(CoreRestTemplate.class);

  private static final OkHttp3ClientHttpRequestFactory factory;
  private static final RestTemplate restTemplate;

  static {
    factory = new OkHttp3ClientHttpRequestFactory();
    restTemplate = new RestTemplate(factory);
  }

  private CoreRestTemplate() {
  }

  @NotNull
  public static RestTemplate getInstance() {
    return restTemplate;
  }

  public static void initialize() {
  }

  public static void dispose() {
    try {
      factory.destroy();
    } catch (Exception ignore) {
    }
    logger.debug("{CoreRestTemplate} Core rest template disposed");
  }
}
