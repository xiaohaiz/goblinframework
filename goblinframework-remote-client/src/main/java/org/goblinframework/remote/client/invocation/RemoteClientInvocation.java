package org.goblinframework.remote.client.invocation;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.remote.client.service.RemoteServiceClient;
import org.goblinframework.remote.core.protocol.RemoteRequest;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

abstract public class RemoteClientInvocation {

  public RemoteServiceId serviceId;
  public int maxTimeout;
  public RemoteServiceClient client;
  public RIC instruction;

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
  abstract public RemoteRequest createRequest();
}
