package org.goblinframework.embedded.jetty.server;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.embedded.handler.ServletHandler;
import org.goblinframework.webmvc.servlet.GoblinServletRequest;
import org.goblinframework.webmvc.servlet.GoblinServletResponse;
import org.goblinframework.webmvc.util.HttpContentTypes;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

final public class JettyHttpRequestHandler extends AbstractHandler {

  @NotNull private final ServletHandler handler;

  JettyHttpRequestHandler(@NotNull ServletHandler handler) {
    this.handler = handler;
  }

  @Override
  public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    String lookupPath = handler.transformLookupPath(target);
    if (StringUtils.equals(lookupPath, target)) {
      lookupPath = null;
    }
    try {
      GoblinServletRequest servletRequest = new GoblinServletRequest(request, lookupPath);
      GoblinServletResponse servletResponse = new GoblinServletResponse(response);
      handler.handle(servletRequest, servletResponse);
    } catch (Exception ex) {
      response.resetBuffer();
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.setContentType(HttpContentTypes.TEXT_PLAIN);
      ex.printStackTrace(response.getWriter());
    } finally {
      response.flushBuffer();
    }
  }
}
