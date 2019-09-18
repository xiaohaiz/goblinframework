package org.goblinframework.test.runner;

import org.goblinframework.api.test.TestContext;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

final class TestContextDelegate implements TestContext {

  private final org.springframework.test.context.TestContext delegate;

  TestContextDelegate(@NotNull org.springframework.test.context.TestContext delegate) {
    this.delegate = delegate;
  }

  @Override
  public Object getApplicationContext() {
    return delegate.getApplicationContext();
  }

  @Override
  public Class<?> getTestClass() {
    return delegate.getTestClass();
  }

  @Override
  public Object getTestInstance() {
    return delegate.getTestInstance();
  }

  @Override
  public Method getTestMethod() {
    return delegate.getTestMethod();
  }

  @Override
  public Throwable getTestException() {
    return delegate.getTestException();
  }
}
