package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.api.sync.RedisCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.api.monitor.Instruction;
import org.goblinframework.cache.redis.module.monitor.RDS;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final public class RedisCommandsInterceptor implements MethodInterceptor {

  private final RedisCommands target;

  RedisCommandsInterceptor(@NotNull RedisCommands target) {
    this.target = target;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return target.toString();
    }
    try (RDS instruction = new RDS(Instruction.Mode.SYN)) {
      instruction.operation = method.getName();
      Object[] arguments = invocation.getArguments();
      RedisCommandsMethodPool.extractKeysIfNecessary(instruction, method, arguments);
      return ReflectionUtils.invoke(target, method, arguments);
    }
  }
}
