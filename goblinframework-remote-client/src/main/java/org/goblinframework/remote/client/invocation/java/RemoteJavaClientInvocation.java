package org.goblinframework.remote.client.invocation.java;

import org.goblinframework.api.core.SerializerMode;
import org.goblinframework.remote.client.invocation.RemoteClientInvocation;
import org.goblinframework.remote.client.module.runtime.RemoteServiceMethodInformation;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;

public class RemoteJavaClientInvocation extends RemoteClientInvocation {

  public Method method;
  public Object[] arguments;
  public RemoteServiceMethodInformation methodInformation;

  public RemoteJavaClientInvokerFuture future;

  @Override
  public SerializerMode encoder() {
    return methodInformation.getEncoder();
  }

  @Override
  public long timeout() {
    return methodInformation.getTimeout();
  }

  public int retries() {
    return methodInformation.getRetries();
  }

  @Override
  public boolean asynchronous() {
    return methodInformation.getAsynchronous();
  }

  @Override
  public boolean noResponseWait() {
    return methodInformation.getNoResponseWait();
  }

  @Override
  public boolean dispatchAll() {
    return methodInformation.getDispatchAll();
  }

  @Override
  public boolean ignoreNoProvider() {
    return methodInformation.getIgnoreNoProvider();
  }

  @Override
  public void reset() {
    super.reset();
    future = null;
  }

  @Override
  public void initializeFuture() {
    this.future = new RemoteJavaClientInvokerFuture(maxTimeout);
    this.future.instruction = instruction;
  }

  @Override
  public void complete(@Nullable Object result) {
    future.complete(result);
  }

  @Override
  public void complete(@Nullable Object result, @Nullable Throwable cause) {
    future.complete(result, cause);
  }
}
