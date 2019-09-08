package org.goblinframework.core.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.Method;

final public class ReflectionUtils {

  public static boolean isToStringMethod(@Nullable final Method method) {
    return (method != null && method.getName().equals("toString") && method.getParameterTypes().length == 0);
  }

  @SuppressWarnings("unchecked")
  @NotNull
  public static <T> T createProxy(@NotNull final Class<T> interfaceClass,
                                  @NotNull final MethodInterceptor interceptor) {
    if (!interfaceClass.isInterface()) {
      throw new IllegalArgumentException(interfaceClass.getName() + " is not interface");
    }
    ProxyFactory proxyFactory = new ProxyFactory();
    proxyFactory.setInterfaces(interfaceClass);
    proxyFactory.addAdvice(interceptor);
    return (T) proxyFactory.getProxy(ClassUtils.getDefaultClassLoader());
  }
}
