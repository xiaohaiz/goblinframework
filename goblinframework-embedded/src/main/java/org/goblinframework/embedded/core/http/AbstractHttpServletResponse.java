package org.goblinframework.embedded.core.http;

import kotlin.text.Charsets;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.nio.charset.Charset;
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
}
