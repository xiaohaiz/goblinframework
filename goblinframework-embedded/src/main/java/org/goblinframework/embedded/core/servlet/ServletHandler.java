package org.goblinframework.embedded.core.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface ServletHandler {

  default String transformTarget(String target) {
    return target;
  }

  void handle(String target, HttpServletRequest request, HttpServletResponse response)
      throws IOException, ServletException;

}
