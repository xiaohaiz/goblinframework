package org.goblinframework.cache.core.enhance;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.goblinframework.cache.core.support.GoblinCacheBean;
import org.goblinframework.core.util.ReflectionUtils;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final class GoblinCacheInterceptor implements MethodInterceptor {

  @NotNull private final Object target;
  @NotNull private final GoblinCacheBean capsule;

  GoblinCacheInterceptor(@NotNull Object target, @NotNull GoblinCacheBean capsule) {
    this.target = target;
    this.capsule = capsule;
  }

  @Override
  public Object invoke(MethodInvocation invocation) throws Throwable {
    Method method = invocation.getMethod();
    if (ReflectionUtils.isToStringMethod(method)) {
      return "GoblinCacheInterceptor(" + target + ")";
    }
    Object[] arguments = invocation.getArguments();
    return ReflectionUtils.invoke(target, method, arguments);
  }
}
