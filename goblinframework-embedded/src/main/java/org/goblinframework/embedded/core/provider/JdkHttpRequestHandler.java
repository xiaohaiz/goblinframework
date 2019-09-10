package org.goblinframework.embedded.core.provider;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import org.goblinframework.embedded.core.setting.ServerSetting;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class JdkHttpRequestHandler implements HttpHandler {

  private final ServerSetting setting;

  JdkHttpRequestHandler(@NotNull ServerSetting setting) {
    this.setting = setting;
  }

  @Override
  public void handle(HttpExchange exchange) throws IOException {
//    String path = HttpUtils.compactContinuousSlashes(exchange.getRequestURI().getPath());
//
//    JdkHttpServletResponse response = new JdkHttpServletResponse(exchange);
//    if (HttpUtils.isMalformedPath(path)) {
//      response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Malformed request path specified");
//      response.close();
//      return;
//    }
//
//    HandlerSettings handlerSettings = setting.handlerSettings();
//    ImmutablePair<String, String> pair = handlerSettings.lookupContextPath(path);
//    String contextPath = pair.getLeft();
//    String target = pair.getRight();
//    HandlerSetting handlerSetting = handlerSettings.get(contextPath);
//    if (handlerSetting == null) {
//      response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unrecognized contextPath specified");
//      response.close();
//      return;
//    }
//
//    ServletHandler handler = handlerSetting.servletHandler();
//    JdkHttpServletRequest request = new JdkHttpServletRequest(exchange, contextPath, handler.transformTarget(target));
//    try {
//      handler.handle(request, response);
//    } catch (Exception ex) {
//      response.resetBuffer();
//      ex.printStackTrace(response.getWriter());
//      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//    }
//    try {
//      response.close();
//    } catch (Exception ignore) {
//    }
  }
}
