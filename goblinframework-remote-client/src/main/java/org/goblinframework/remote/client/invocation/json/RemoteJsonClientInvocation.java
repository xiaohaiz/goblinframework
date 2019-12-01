package org.goblinframework.remote.client.invocation.json;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.remote.client.invocation.RemoteClientInvocation;
import org.jetbrains.annotations.Nullable;

public class RemoteJsonClientInvocation extends RemoteClientInvocation {

  @Override
  public SerializerMode encoder() {
    return null;
  }

  @Override
  public long timeout() {
    return 0;
  }

  @Override
  public boolean asynchronous() {
    return false;
  }

  @Override
  public boolean noResponseWait() {
    return false;
  }

  @Override
  public boolean dispatchAll() {
    return false;
  }

  @Override
  public boolean ignoreNoProvider() {
    return false;
  }

  @Override
  public void initializeFuture() {

  }

  @Override
  public void complete(@Nullable Object result) {

  }

  @Override
  public void complete(@Nullable Object result, @Nullable Throwable cause) {

  }
}
