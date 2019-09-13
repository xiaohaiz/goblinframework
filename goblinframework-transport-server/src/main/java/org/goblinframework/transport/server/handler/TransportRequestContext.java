package org.goblinframework.transport.server.handler;

import org.goblinframework.transport.core.protocol.TransportMessage;
import org.goblinframework.transport.core.protocol.TransportResponse;
import org.goblinframework.transport.core.protocol.reader.TransportRequestReader;
import org.goblinframework.transport.core.protocol.writer.TransportResponseWriter;
import org.goblinframework.transport.server.channel.TransportServerChannel;

import java.util.concurrent.ConcurrentHashMap;

public class TransportRequestContext {

  public TransportServerChannel channel;
  public byte serializer;
  public TransportRequestReader requestReader;
  public TransportResponseWriter responseWriter;
  public ConcurrentHashMap<String, Object> extensions;

  public void sendResponse() {
    if (!requestReader.request().response) {
      // no response required
      return;
    }
    TransportResponse response = responseWriter.response();
    channel.writeTransportMessage(new TransportMessage(response, serializer));
  }
}
