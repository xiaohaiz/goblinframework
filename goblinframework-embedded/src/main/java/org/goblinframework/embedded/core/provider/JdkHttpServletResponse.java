package org.goblinframework.embedded.core.provider;

import com.sun.net.httpserver.HttpExchange;
import org.goblinframework.core.util.ArrayUtils;
import org.goblinframework.embedded.core.servlet.AbstractHttpServletResponse;
import org.goblinframework.http.util.SendError;
import org.goblinframework.webmvc.util.HttpContentTypes;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicReference;

public class JdkHttpServletResponse extends AbstractHttpServletResponse implements Closeable {

  private final HttpExchange exchange;
  private final JdkServletOutputStream outputStream;
  private final PrintWriter writer;
  private final AtomicReference<SendError> sendError = new AtomicReference<>();

  JdkHttpServletResponse(@NotNull HttpExchange exchange) {
    this.exchange = exchange;
    this.outputStream = new JdkServletOutputStream();
    this.writer = new PrintWriter(this.outputStream);

  }

  @Override
  public ServletOutputStream getOutputStream() {
    return outputStream;
  }

  @Override
  public PrintWriter getWriter() {
    return writer;
  }

  @Override
  public String getHeader(String name) {
    return exchange.getResponseHeaders().getFirst(name);
  }

  @Override
  public void setHeader(String name, String value) {
    exchange.getResponseHeaders().set(name, value);
  }

  @Override
  public void addHeader(String name, String value) {
    exchange.getResponseHeaders().add(name, value);
  }

  @Override
  public void sendError(int sc, String msg) {
    sendError.set(new SendError(sc, msg));
  }

  @Override
  public void sendError(int sc) {
    sendError(sc, null);
  }

  @Override
  public void flushBuffer() {
  }

  @Override
  public void resetBuffer() {
    outputStream.reset();
  }

  @Override
  public void reset() {
    outputStream.reset();
    exchange.getResponseHeaders().clear();
  }

  @Override
  public void close() throws IOException {
    try {
      SendError se = sendError.get();
      if (se != null) {
        reset();
        setStatus(se.getSc());
        byte[] bs = ArrayUtils.EMPTY_BYTE_ARRAY;
        String msg = se.getMsg();
        if (msg != null) {
          setContentType(HttpContentTypes.TEXT_HTML);
          bs = msg.getBytes(StandardCharsets.UTF_8);
          outputStream.write(bs);
        }
        sendResponse(se.getSc(), bs);
      } else {
        writer.flush();
        sendResponse(getStatus(), outputStream.responseBody());
      }
    } finally {
      exchange.close();
    }
  }

  private void sendResponse(int sc, byte[] content) throws IOException {
    int contentLength = content.length;
    if (sc == HttpServletResponse.SC_NOT_MODIFIED) {
      contentLength = -1;
    }
    setContentLength(contentLength);
    exchange.sendResponseHeaders(sc, contentLength);
    if (content.length > 0) {
      exchange.getResponseBody().write(content);
    }
    exchange.getResponseBody().flush();
  }
}
