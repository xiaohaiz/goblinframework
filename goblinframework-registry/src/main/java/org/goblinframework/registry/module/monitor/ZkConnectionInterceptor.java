package org.goblinframework.registry.module.monitor;

import org.I0Itec.zkclient.IZkConnection;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.zookeeper.CreateMode;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final public class ZkConnectionInterceptor implements MethodInterceptor {

  @NotNull private final IZkConnection connection;

  public ZkConnectionInterceptor(@NotNull IZkConnection connection) {
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
      instruction.operation = method.getName();
      switch (mode) {
        case 1: {
          instruction.path = (String) arguments[0];
          CreateMode createMode = (CreateMode) arguments[arguments.length - 1];
          if (createMode != null) {
            instruction.createMode = createMode.name();
          }
          break;
        }
        case 2: {
          instruction.path = (String) arguments[0];
          break;
        }
        default: {
          break;
        }
      }
      return ReflectionUtils.invoke(connection, method, arguments);
    }
  }
}
