package org.goblinframework.remote.client.invocation;

import org.goblinframework.remote.client.module.monitor.RIC;
import org.goblinframework.remote.client.service.RemoteServiceClient;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.Nullable;

abstract public class RemoteClientInvocation {

  public RemoteServiceId serviceId;
  public int maxTimeout;
  public RemoteServiceClient client;
  public RIC instruction;

  abstract public void initializeFuture();

  abstract public void complete(@Nullable Object result);

  abstract public void complete(@Nullable Object result, @Nullable Throwable cause);
}
