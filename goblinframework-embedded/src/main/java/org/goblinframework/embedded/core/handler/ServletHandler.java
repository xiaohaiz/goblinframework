package org.goblinframework.embedded.core.handler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletHandler {

  default String transformTarget(String target) {
    return target;
  }

  void handle(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

}
