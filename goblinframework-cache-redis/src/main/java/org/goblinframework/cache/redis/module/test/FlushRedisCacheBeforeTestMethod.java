package org.goblinframework.cache.redis.module.test;

import org.goblinframework.api.annotation.Install;
import org.goblinframework.api.test.TestContext;
import org.goblinframework.api.test.TestExecutionListener;
import org.goblinframework.cache.redis.client.RedisClient;
import org.goblinframework.cache.redis.client.RedisClientManager;
import org.goblinframework.cache.redis.module.config.RedisConfig;
import org.goblinframework.cache.redis.module.config.RedisConfigManager;

import java.lang.reflect.Method;
import java.util.List;

@Install
final public class FlushRedisCacheBeforeTestMethod implements TestExecutionListener {

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    if (!flushAvailable(testContext)) {
      return;
    }
    List<RedisConfig> configs = RedisConfigManager.INSTANCE.getRedisConfigs();
    if (configs.isEmpty()) {
      return;
    }
    for (RedisConfig config : configs) {
      String name = config.getName();
      RedisClient client = RedisClientManager.INSTANCE.getRedisClient(name);
      if (client != null) {
        client.flush();
      }
    }
  }

  private boolean flushAvailable(TestContext testContext) {
    Method method = testContext.getTestMethod();
    if (method.isAnnotationPresent(FlushRedisCache.class)) {
      return true;
    }
    Class<?> clazz = testContext.getTestClass();
    return clazz.isAnnotationPresent(FlushRedisCache.class);
  }
}
