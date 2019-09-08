package org.goblinframework.embedded.core.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.goblinframework.webmvc.util.UrlUtils;

import java.io.IOException;

public class JdkHttpHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = UrlUtils.compactContinuousSlashes(exchange.getRequestURI().getPath());

  }
}
