package org.goblinframework.remote.client.service;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.remote.client.connection.RemoteConnection;
import org.goblinframework.remote.core.service.RemoteServiceId;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.List;

public class RemoteClientInterceptor implements MethodInterceptor {

  private final RemoteServiceId serviceId;

  RemoteClientInterceptor(@NotNull RemoteServiceId serviceId) {
    this.serviceId = serviceId;

  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "RemoteClientInterceptor(" + serviceId + ")";
    }
    RemoteClientManager clientManager = RemoteClientManager.INSTANCE;
    RemoteClient client = clientManager.getRemoteClient(serviceId);
    List<RemoteConnection> connections = client.availableConnections();
    return null;
  }
}
