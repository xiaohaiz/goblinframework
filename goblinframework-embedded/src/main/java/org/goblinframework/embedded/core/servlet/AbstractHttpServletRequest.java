package org.goblinframework.embedded.core.servlet;

import kotlin.text.Charsets;
import org.apache.http.client.utils.DateUtils;
import org.goblinframework.core.util.StringUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.UnsupportedEncodingException;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

public class AbstractHttpServletRequest extends HttpServletRequestWrapper {

  private static final String DEFAULT_SERVLET_PATH = "";

  private final ConcurrentHashMap<String, Object> attributes = new ConcurrentHashMap<>();
  private final AtomicReference<String> encoding = new AtomicReference<>(Charsets.UTF_8.name());

  protected AbstractHttpServletRequest() {
    super(HttpServletRequestAdapter.INSTANCE.getAdapter());
  }

  @Override
  public Object getAttribute(String name) {
    return attributes.get(name);
  }

  @Override
  public Enumeration<String> getAttributeNames() {
    return Collections.enumeration(attributes.keySet());
  }

  @Override
  public void setAttribute(String name, Object o) {
    attributes.put(name, o);
  }

  @Override
  public void removeAttribute(String name) {
    attributes.remove(name);
  }

  @Override
  public String getCharacterEncoding() {
    return encoding.get();
  }

  @Override
  public void setCharacterEncoding(String enc) throws UnsupportedEncodingException {
    if (!Charset.isSupported(enc)) {
      throw new UnsupportedEncodingException();
    }
    encoding.set(enc);
  }

  @Override
  public String getContentType() {
    return getHeader(HttpHeaders.CONTENT_TYPE);
  }

  @Override
  public int getContentLength() {
    return getIntHeader(HttpHeaders.CONTENT_LENGTH);
  }

  @Override
  public long getContentLengthLong() {
    return getContentLength();
  }

  @Override
  public long getDateHeader(String name) {
    String header = getHeader(name);
    if (header == null) {
      return -1;
    }
    try {
      Date date = DateUtils.parseDate(header);
      return date == null ? -1 : date.getTime();
    } finally {
      DateUtils.clearThreadLocal();
    }
  }

  @Override
  public int getIntHeader(String name) {
    String header = getHeader(name);
    if (header == null) {
      return -1;
    }
    try {
      return Integer.parseInt(header);
    } catch (Exception ex) {
      return -1;
    }
  }

  @Override
  public Locale getLocale() {
    return Locale.getDefault();
  }

  @Override
  public Enumeration<Locale> getLocales() {
    return Collections.enumeration(Collections.singleton(getLocale()));
  }

  @Override
  public String getServletPath() {
    return DEFAULT_SERVLET_PATH;
  }

  @Override
  public Cookie[] getCookies() {
    String header = getHeader(HttpHeaders.COOKIE);
    if (StringUtils.isBlank(header)) {
      return null;
    }
    List<HttpCookie> cookies = HttpCookie.parse(header);
    if (cookies.isEmpty()) {
      return null;
    }
    return cookies.stream()
        .map(e -> {
          Cookie cookie = new Cookie(e.getName(), e.getValue());
          cookie.setComment(e.getComment());
          cookie.setDomain(e.getDomain());
          cookie.setMaxAge(e.getVersion());
          cookie.setPath(e.getPath());
          cookie.setSecure(e.getSecure());
          cookie.setVersion(e.getVersion());
          cookie.setHttpOnly(e.isHttpOnly());
          return cookie;
        })
        .toArray(Cookie[]::new);
  }
}
