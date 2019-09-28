package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.api.async.RedisAsyncCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.monitor.Instruction;
import org.goblinframework.cache.redis.module.monitor.RDS;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final public class RedisAsyncCommandsInterceptor implements MethodInterceptor {

  private final RedisAsyncCommands target;

  RedisAsyncCommandsInterceptor(@NotNull RedisAsyncCommands target) {
    this.target = target;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "RedisAsyncCommandsInterceptor->" + target;
    }
    RDS rds;
    if (method.getReturnType() == RedisFuture.class) {
      rds = new RDS(Instruction.Mode.ASY);
    } else {
      rds = new RDS(Instruction.Mode.SYN);
    }
    rds.operation = method.getName();

    Object[] arguments = invocation.getArguments();
    RedisCommandsMethodPool.extractKeysIfNecessary(rds, method, arguments);
    try {
      Object ret = ReflectionUtils.invoke(target, method, arguments);
      if (rds.mode() == Instruction.Mode.ASY && ret instanceof RedisFuture) {
        RedisFuture redisFuture = (RedisFuture) ret;
        redisFuture.thenRun(rds::complete);
      }
      return ret;
    } finally {
      rds.close();
    }
  }
}
