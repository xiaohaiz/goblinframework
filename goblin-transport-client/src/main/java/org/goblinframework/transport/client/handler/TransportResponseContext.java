package org.goblinframework.transport.client.handler;

import org.goblinframework.transport.protocol.TransportResponse;

import java.util.concurrent.ConcurrentHashMap;

public class TransportResponseContext {

  public TransportResponse response;
  public ConcurrentHashMap<String, Object> extensions;

}
