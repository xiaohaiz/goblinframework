package org.goblinframework.remote.client.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class RemoteClientInterceptor implements MethodInterceptor {

  private final RemoteServiceId serviceId;
  private final RemoteClientInvoker invoker;

  RemoteClientInterceptor(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;
    this.invoker = new RemoteClientInvoker(serviceId);
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "RemoteClientInterceptor(" + serviceId + ")";
    }

    return invoker.invoke(invocation);
  }
}
