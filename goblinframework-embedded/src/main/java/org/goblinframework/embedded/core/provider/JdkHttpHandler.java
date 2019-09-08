package org.goblinframework.embedded.core.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.goblinframework.core.util.StringUtils;

import java.io.IOException;

public class JdkHttpHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = StringUtils.compactContinuousSlashes(exchange.getRequestURI().getPath());
  }
}
