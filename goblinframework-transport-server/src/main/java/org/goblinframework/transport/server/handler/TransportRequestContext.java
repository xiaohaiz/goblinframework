package org.goblinframework.transport.server.handler;

import org.goblinframework.transport.core.protocol.reader.TransportRequestReader;
import org.goblinframework.transport.server.channel.TransportServerChannel;

import java.util.concurrent.ConcurrentHashMap;

public class TransportRequestContext {

  public TransportServerChannel channel;
  public TransportRequestReader requestReader;
  public ConcurrentHashMap<String, Object> extensions;

}
