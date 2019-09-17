package org.goblinframework.cache.redis.provider;

import org.goblinframework.cache.core.cache.CacheSystem;
import org.goblinframework.cache.core.cache.CacheSystemLocation;
import org.goblinframework.cache.core.cache.GoblinCacheImpl;
import org.goblinframework.cache.redis.client.RedisClient;
import org.goblinframework.core.mbean.GoblinManagedBean;
import org.jetbrains.annotations.NotNull;

@GoblinManagedBean(type = "CACHE.REDIS")
final class RedisCacheImpl extends GoblinCacheImpl {

  private final RedisClient client;

  RedisCacheImpl(@NotNull String name, @NotNull RedisClient client) {
    super(new CacheSystemLocation(CacheSystem.RDS, name));
    this.client = client;
  }

  void destroy() {
    unregisterIfNecessary();
    logger.debug("REDIS cache [{}] closed", getName());
  }

}
