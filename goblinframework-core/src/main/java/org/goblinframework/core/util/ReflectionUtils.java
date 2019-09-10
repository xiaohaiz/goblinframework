package org.goblinframework.core.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.aop.framework.ProxyFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

abstract public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

  @Nullable
  public static Object invoke(@Nullable Object target,
                              @NotNull Method method,
                              @Nullable Object... args) throws Throwable {
    if (!method.isAccessible()) {
      method.setAccessible(true);
    }
    try {
      return method.invoke(target, args);
    } catch (InvocationTargetException ex) {
      throw ex.getTargetException();
    }
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
