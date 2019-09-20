package org.goblinframework.cache.core.module.test;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.test.TestContext;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.cache.core.provider.InJvmCache;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@Install
final public class FlushInJvmCacheBeforeTestMethod implements TestExecutionListener {

  @Override
  public void beforeTestMethod(@NotNull TestContext testContext) {
    if (!flushAvailable(testContext)) {
      return;
    }
    InJvmCache.INSTANCE.flush();
  }

  private boolean flushAvailable(TestContext testContext) {
    Method method = testContext.getTestMethod();
    if (method.isAnnotationPresent(FlushInJvmCache.class)) {
      return true;
    }
    Class<?> clazz = testContext.getTestClass();
    return clazz.isAnnotationPresent(FlushInJvmCache.class);
  }
}
