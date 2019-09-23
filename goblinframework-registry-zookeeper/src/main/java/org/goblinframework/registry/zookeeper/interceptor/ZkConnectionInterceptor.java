package org.goblinframework.registry.zookeeper.interceptor;

import org.I0Itec.zkclient.IZkConnection;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ReflectionUtils;
import org.goblinframework.registry.zookeeper.module.monitor.ZKP;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

public class ZkConnectionInterceptor implements MethodInterceptor {

  private final String name;
  private final IZkConnection connection;

  public ZkConnectionInterceptor(@NotNull String name, @NotNull IZkConnection connection) {
    this.name = name;
    this.connection = connection;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "ZkConnectionInterceptor(" + connection + ")";
    }
    Object[] arguments = invocation.getArguments();
    int mode = ZkConnectionMethodPool.mode(method, -1);
    if (mode == -1) {
      return ReflectionUtils.invoke(connection, method, arguments);
    }
    try (ZKP instruction = new ZKP()) {
      instruction.setName(name);
      instruction.setOperation(method.getName());
      if (mode == 1) {
        String path = (String) arguments[0];
        instruction.setPath(path);
      }
      return ReflectionUtils.invoke(connection, method, arguments);
    }
  }
}
