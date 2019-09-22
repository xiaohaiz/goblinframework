package org.goblinframework.cache.core.module.test;

import org.goblinframework.api.annotation.Singleton;
import org.goblinframework.api.test.TestContext;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.cache.core.provider.InJvmCache;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Method;

@Singleton
final public class FlushInJvmCacheBeforeTestMethod implements TestExecutionListener {

  public static final FlushInJvmCacheBeforeTestMethod INSTANCE = new FlushInJvmCacheBeforeTestMethod();

  private FlushInJvmCacheBeforeTestMethod() {
  }

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
