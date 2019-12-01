package org.goblinframework.remote.client.invocation;

import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.Nullable;

abstract public class RemoteClientInvocation {

  public RemoteServiceId serviceId;

  abstract public void complete(@Nullable Object result);

  abstract public void complete(@Nullable Object result, @Nullable Throwable cause);
}
