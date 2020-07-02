package org.goblinframework.core.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.jetbrains.annotations.NotNull;
import org.springframework.aop.framework.ProxyFactory;

/**
 * Proxy related helper utilities.
 *
 * @author Xiaohai Zhang
 * @since Dec 6, 2019
 */
abstract public class ProxyUtils {

  /**
   * Create proxy for specified interface with {@link MethodInterceptor} instance.
   *
   * @param interfaceClass Interface class
   * @param interceptor    Method interceptor instance
   * @return Proxied interface
   */
  @SuppressWarnings("unchecked")
  @NotNull
  public static <T> T createInterfaceProxy(@NotNull final Class<T> interfaceClass,
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
