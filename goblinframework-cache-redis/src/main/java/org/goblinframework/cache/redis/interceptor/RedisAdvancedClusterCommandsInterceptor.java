package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.monitor.Instruction;
import org.goblinframework.cache.redis.module.monitor.RDS;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final public class RedisAdvancedClusterCommandsInterceptor implements MethodInterceptor {

  private final RedisAdvancedClusterCommands target;

  public RedisAdvancedClusterCommandsInterceptor(@NotNull RedisAdvancedClusterCommands target) {
    this.target = target;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return target.toString();
    }
    try (RDS rds = new RDS(Instruction.Mode.SYN)) {
      rds.operation = method.getName();

      Object[] arguments = invocation.getArguments();
      RedisCommandsMethodPool.extractKeysIfNecessary(rds, method, arguments);
      return ReflectionUtils.invoke(target, method, arguments);
    }
  }
}
