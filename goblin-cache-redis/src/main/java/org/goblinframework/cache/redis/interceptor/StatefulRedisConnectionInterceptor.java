package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ProxyUtils;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

final public class StatefulRedisConnectionInterceptor implements MethodInterceptor {

  private static final Map<Method, Integer> methodPool;

  static {
    methodPool = new HashMap<>();
    try {
      methodPool.put(StatefulRedisConnection.class.getMethod("sync"), 1);
      methodPool.put(StatefulRedisConnection.class.getMethod("async"), 2);
    } catch (NoSuchMethodException ex) {
      throw new IllegalStateException(ex);
    }
  }


  private final StatefulRedisConnection target;
  private final RedisCommands proxy_redisCommands;
  private final RedisAsyncCommands proxy_redisAsyncCommands;

  public StatefulRedisConnectionInterceptor(@NotNull StatefulRedisConnection target) {
    this.target = Objects.requireNonNull(target);

    RedisCommands redisCommands = target.sync();
    this.proxy_redisCommands = ProxyUtils.createInterfaceProxy(RedisCommands.class, new RedisCommandsInterceptor(redisCommands));

    RedisAsyncCommands redisAsyncCommands = target.async();
    this.proxy_redisAsyncCommands = ProxyUtils.createInterfaceProxy(RedisAsyncCommands.class, new RedisAsyncCommandsInterceptor(redisAsyncCommands));
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return target.toString();
    }
    int mode = methodPool.getOrDefault(method, 0);
    switch (mode) {
      case 1: {
        return proxy_redisCommands;
      }
      case 2: {
        return proxy_redisAsyncCommands;
      }
      default: {
        Object[] arguments = invocation.getArguments();
        return ReflectionUtils.invoke(target, method, arguments);
      }
    }
  }
}
