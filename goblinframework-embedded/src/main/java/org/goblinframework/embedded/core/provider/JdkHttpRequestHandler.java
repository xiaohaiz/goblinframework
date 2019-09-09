package org.goblinframework.embedded.core.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.goblinframework.http.util.HttpUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JdkHttpRequestHandler implements HttpHandler {

  @Override
  public void handle(HttpExchange exchange) throws IOException {
    String path = HttpUtils.compactContinuousSlashes(exchange.getRequestURI().getPath());

    JdkHttpServletResponse response = new JdkHttpServletResponse(exchange);
    if (HttpUtils.isMalformedPath(path)) {
      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request path specified");
      response.close();
      return;
    }
  }
}
