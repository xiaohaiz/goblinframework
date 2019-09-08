package org.goblinframework.core.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.ProxyFactory;

final public class ReflectionUtils extends org.springframework.util.ReflectionUtils {

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
