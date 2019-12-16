package org.goblinframework.remote.client.invocation;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.remote.client.service.RemoteServiceClient;
import org.goblinframework.rpc.protocol.RpcRequest;
import org.goblinframework.rpc.service.RemoteServiceId;
import org.goblinframework.transport.client.channel.TransportClient;
import org.goblinframework.transport.client.flight.MessageFlight;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

abstract public class RemoteClientInvocation {

  public RemoteServiceId serviceId;
  public int maxTimeout;
  public RemoteServiceClient client;
  public RIC instruction;
  public byte[] encodedRequest;
  public MessageFlight flight;
  public List<TransportClient> routes;
  public Throwable transportError;

  public void reset() {
    client = null;
    instruction = null;
  }

  abstract public SerializerMode encoder();

  abstract public long timeout();

  abstract public boolean asynchronous();

  abstract public boolean noResponseWait();

  abstract public boolean dispatchAll();

  abstract public boolean ignoreNoProvider();

  abstract public void initializeFuture();

  abstract public void complete(@Nullable Object result);

  abstract public void complete(@Nullable Object result, @Nullable Throwable cause);

  @NotNull
  abstract public RpcRequest createRequest();

  abstract public String asMethodText();
}
