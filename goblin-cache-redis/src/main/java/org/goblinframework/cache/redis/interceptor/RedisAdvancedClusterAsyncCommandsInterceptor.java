package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.RedisFuture;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.cache.redis.module.monitor.RDS;
import org.goblinframework.core.monitor.Instruction;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final public class RedisAdvancedClusterAsyncCommandsInterceptor implements MethodInterceptor {

  private final RedisAdvancedClusterAsyncCommands commands;

  public RedisAdvancedClusterAsyncCommandsInterceptor(@NotNull RedisAdvancedClusterAsyncCommands commands) {
    this.commands = commands;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return commands.toString();
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
      Object ret = ReflectionUtils.invoke(commands, method, arguments);
      if (ret instanceof RedisFuture && rds.mode() == Instruction.Mode.ASY) {
        RedisFuture redisFuture = (RedisFuture) ret;
        redisFuture.thenRun(rds::complete);
      }
      return ret;
    } finally {
      rds.close();
    }
  }
}
