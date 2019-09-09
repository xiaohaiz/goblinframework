package org.goblinframework.embedded.core.servlet;

import kotlin.text.Charsets;
import org.apache.http.client.utils.DateUtils;
import org.springframework.http.HttpHeaders;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.net.HttpCookie;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

abstract public class AbstractHttpServletResponse extends HttpServletResponseWrapper {

  private final AtomicInteger status = new AtomicInteger(HttpServletResponse.SC_OK);
  private final AtomicReference<String> encoding = new AtomicReference<>(Charsets.UTF_8.name());
  private final AtomicReference<Locale> locale = new AtomicReference<>(Locale.getDefault());

  protected AbstractHttpServletResponse() {
    super(HttpServletResponseAdapter.INSTANCE.getAdapter());
  }

  @Override
  public String getCharacterEncoding() {
    return encoding.get();
  }

  @Override
  public void setCharacterEncoding(String charset) {
    if (Charset.isSupported(charset)) {
      encoding.set(charset);
    }
  }

  @Override
  public void setLocale(Locale loc) {
    if (loc != null) {
      locale.set(loc);
    }
    super.setLocale(loc);
  }

  @Override
  public Locale getLocale() {
    return locale.get();
  }

  @Override
  public void setStatus(int sc) {
    status.set(sc);
  }

  @Override
  @Deprecated
  public void setStatus(int sc, String sm) {
    setStatus(sc);
  }

  @Override
  public int getStatus() {
    return status.get();
  }

  @Override
  public boolean containsHeader(String name) {
    return getHeader(name) != null;
  }

  @Override
  public String getContentType() {
    return getHeader(HttpHeaders.CONTENT_TYPE);
  }

  @Override
  public void setContentType(String type) {
    setHeader(HttpHeaders.CONTENT_TYPE, type);
  }

  @Override
  public void setContentLength(int len) {
    setIntHeader(HttpHeaders.CONTENT_LENGTH, len);
  }

  @Override
  public void setContentLengthLong(long len) {
    setContentLength((int) len);
  }

  @Override
  public void addIntHeader(String name, int value) {
    addHeader(name, Integer.toString(value));
  }

  @Override
  public void setIntHeader(String name, int value) {
    setHeader(name, Integer.toString(value));
  }

  @Override
  public void addDateHeader(String name, long date) {
    try {
      addHeader(name, DateUtils.formatDate(new Date(date)));
    } finally {
      DateUtils.clearThreadLocal();
    }
  }

  @Override
  public void setDateHeader(String name, long date) {
    try {
      setHeader(name, DateUtils.formatDate(new Date(date)));
    } finally {
      DateUtils.clearThreadLocal();
    }
  }

  @Override
  public void sendRedirect(String location) {
    resetBuffer();
    setContentLength(0);
    setHeader(HttpHeaders.LOCATION, location);
    setStatus(HttpServletResponse.SC_MOVED_TEMPORARILY);
  }

  @Override
  public void addCookie(Cookie cookie) {
    HttpCookie hc = new HttpCookie(cookie.getName(), cookie.getValue());
    hc.setComment(cookie.getComment());
    hc.setDomain(cookie.getDomain());
    hc.setMaxAge(cookie.getMaxAge());
    hc.setPath(cookie.getPath());
    hc.setSecure(cookie.getSecure());
    hc.setVersion(cookie.getVersion());
    hc.setHttpOnly(cookie.isHttpOnly());
    addHeader(HttpHeaders.SET_COOKIE, hc.toString());
  }
}
