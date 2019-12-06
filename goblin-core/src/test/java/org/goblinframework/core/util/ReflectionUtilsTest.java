package org.goblinframework.core.util;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.Test;
import org.springframework.aop.framework.ReflectiveMethodInvocation;

import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;

public class ReflectionUtilsTest {

  interface MockInterface {

    default String getName() {
      return "HELLO";
    }

  }

  @Test
  public void invokeInterfaceDefaultMethod() {
    MethodInterceptor interceptor = methodInvocation -> {
      Method method = methodInvocation.getMethod();
      if (ReflectionUtils.isToStringMethod(method)) {
        return "ReflectionUtilsTest.MockInterface";
      }
      Object[] arguments = methodInvocation.getArguments();
      if (method.isDefault()) {
        Object proxy = ((ReflectiveMethodInvocation) methodInvocation).getProxy();
        return ReflectionUtils.invokeInterfaceDefaultMethod(proxy, method, arguments);
      }
      throw new UnsupportedOperationException();
    };
    MockInterface mi = ReflectionUtils.createProxy(MockInterface.class, interceptor);
    String name = mi.getName();
    assertEquals("HELLO", name);
  }
}