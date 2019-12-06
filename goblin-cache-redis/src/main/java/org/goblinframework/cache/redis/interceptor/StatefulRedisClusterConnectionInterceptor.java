package org.goblinframework.cache.redis.interceptor;

import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.core.util.ProxyUtils;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

final public class StatefulRedisClusterConnectionInterceptor implements MethodInterceptor {

  private static final Map<Method, Integer> methodPool;

  static {
    methodPool = new HashMap<>();
    try {
      methodPool.put(StatefulRedisClusterConnection.class.getMethod("getConnection", String.class, int.class), 1);
      methodPool.put(StatefulRedisClusterConnection.class.getMethod("getConnection", String.class), 2);
      methodPool.put(StatefulRedisClusterConnection.class.getMethod("sync"), 3);
      methodPool.put(StatefulRedisClusterConnection.class.getMethod("async"), 4);
    } catch (NoSuchMethodException ex) {
      throw new IllegalStateException(ex);
    }
  }

  private final StatefulRedisClusterConnection target;
  private final RedisAdvancedClusterCommands proxy_redisAdvancedClusterCommands;
  private final RedisAdvancedClusterAsyncCommands proxy_redisAdvancedClusterAsyncCommands;

  public StatefulRedisClusterConnectionInterceptor(@NotNull StatefulRedisClusterConnection target) {
    this.target = target;

    RedisAdvancedClusterCommands redisAdvancedClusterCommands = target.sync();
    RedisAdvancedClusterAsyncCommands redisAdvancedClusterAsyncCommands = target.async();

    this.proxy_redisAdvancedClusterCommands = ProxyUtils.createInterfaceProxy(RedisAdvancedClusterCommands.class, new RedisAdvancedClusterCommandsInterceptor(redisAdvancedClusterCommands));

    this.proxy_redisAdvancedClusterAsyncCommands = ProxyUtils.createInterfaceProxy(RedisAdvancedClusterAsyncCommands.class, new RedisAdvancedClusterAsyncCommandsInterceptor(redisAdvancedClusterAsyncCommands));
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return target.toString();
    }

    int mode = methodPool.getOrDefault(method, 0);
    switch (mode) {
      case 1:
      case 2: {
        Object[] arguments = invocation.getArguments();
        StatefulRedisConnection c = (StatefulRedisConnection) ReflectionUtils.invoke(target, method, arguments);
        if (c == null) {
          return null;
        }
        StatefulRedisConnectionInterceptor interceptor = new StatefulRedisConnectionInterceptor(c);
        return ProxyUtils.createInterfaceProxy(StatefulRedisConnection.class, interceptor);
      }
      case 3: {
        return proxy_redisAdvancedClusterCommands;
      }
      case 4: {
        return proxy_redisAdvancedClusterAsyncCommands;
      }
      default: {
        Object[] arguments = invocation.getArguments();
        return ReflectionUtils.invoke(target, method, arguments);
      }
    }

  }
}
