package org.goblinframework.embedded.core.provider;

import com.sun.net.httpserver.HttpExchange;
import org.goblinframework.core.util.StringUtils;
import org.goblinframework.embedded.core.servlet.AbstractHttpServletRequest;
import org.goblinframework.http.util.HttpUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.util.LinkedMultiValueMap;

import javax.servlet.ServletInputStream;
import java.net.InetSocketAddress;
import java.util.*;

public class JdkHttpServletRequest extends AbstractHttpServletRequest {

  private final HttpExchange exchange;
  private final JdkServletInputStream inStream;
  private final LinkedMultiValueMap<String, String> parameters;

  JdkHttpServletRequest(@NotNull HttpExchange exchange) {
    this.exchange = exchange;
    this.inStream = new JdkServletInputStream(this.exchange);
    this.parameters = new LinkedMultiValueMap<>();
    String qs = getQueryString();
    if (StringUtils.isNotEmpty(qs)) {
      HttpUtils.parseMultiQueryString(qs).forEach((name, values) -> {
        for (String value : values) {
          this.parameters.add(name, value);
        }
      });
    }
    String contentType = StringUtils.defaultString(getContentType());
    if ("POST".equalsIgnoreCase(getMethod())
        && contentType.contains("application/x-www-form-urlencoded")) {
      HttpUtils.parseMultiQueryString(inStream.requestBodyAsString()).forEach((name, values) -> {
        for (String value : values) {
          this.parameters.add(name, value);
        }
      });
    }
  }

  @Override
  public boolean isSecure() {
    return false;
  }

  @Override
  public ServletInputStream getInputStream() {
    return inStream;
  }

  @Override
  public String getParameter(String name) {
    return parameters.getFirst(name);
  }

  @Override
  public Enumeration<String> getParameterNames() {
    return Collections.enumeration(parameters.keySet());
  }

  @Override
  public String[] getParameterValues(String name) {
    List<String> values = parameters.get(name);
    if (values == null) {
      return null;
    }
    return values.toArray(new String[0]);
  }

  @Override
  public Map<String, String[]> getParameterMap() {
    Map<String, String[]> map = new HashMap<>();
    parameters.forEach((name, value) -> {
      String[] s = value.toArray(new String[0]);
      map.put(name, s);
    });
    return map;
  }

  @Override
  public String getScheme() {
    return exchange.getProtocol();
  }

  @Override
  public String getRemoteHost() {
    InetSocketAddress remote = exchange.getRemoteAddress();
    return remote == null ? null : remote.getHostName();
  }

  @Override
  public int getRemotePort() {
    InetSocketAddress remote = exchange.getRemoteAddress();
    return remote == null ? -1 : remote.getPort();
  }

  @Override
  public String getHeader(String name) {
    return exchange.getRequestHeaders().getFirst(name);
  }

  @Override
  public Enumeration<String> getHeaders(String name) {
    return Collections.enumeration(exchange.getRequestHeaders().get(name));
  }

  @Override
  public Enumeration<String> getHeaderNames() {
    return Collections.enumeration(exchange.getRequestHeaders().keySet());
  }

  @Override
  public String getMethod() {
    return exchange.getRequestMethod();
  }

  @Override
  public String getContextPath() {
    throw new UnsupportedOperationException();
  }

  @Override
  public String getQueryString() {
    return exchange.getRequestURI().getQuery();
  }

  @Override
  public String getRequestURI() {
    throw new UnsupportedOperationException();
  }
}
