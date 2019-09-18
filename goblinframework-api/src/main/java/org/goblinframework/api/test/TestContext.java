package org.goblinframework.api.test;

import java.lang.reflect.Method;

public interface TestContext {

  Object getApplicationContext();

  Class<?> getTestClass();

  Object getTestInstance();

  Method getTestMethod();

  Throwable getTestException();
}
