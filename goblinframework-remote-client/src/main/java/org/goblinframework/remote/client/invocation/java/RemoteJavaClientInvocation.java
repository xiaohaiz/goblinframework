package org.goblinframework.remote.client.invocation.java;

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
