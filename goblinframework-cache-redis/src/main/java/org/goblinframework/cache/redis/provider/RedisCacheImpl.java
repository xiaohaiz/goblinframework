package org.goblinframework.cache.redis.provider;

import org.goblinframework.cache.core.cache.CacheSystem;
import org.goblinframework.cache.core.cache.CacheSystemLocation;
import org.goblinframework.cache.core.cache.GoblinCache;
import org.goblinframework.cache.redis.client.RedisClient;
import org.jetbrains.annotations.NotNull;

final class RedisCacheImpl implements GoblinCache {

  private final CacheSystemLocation location;
  private final RedisClient client;

  RedisCacheImpl(@NotNull String name, @NotNull RedisClient client) {
    this.location = new CacheSystemLocation(CacheSystem.RDS, name);
    this.client = client;
  }

  @NotNull
  @Override
  public CacheSystemLocation getCacheSystemLocation() {
    return location;
  }

  void destroy() {
  }

}
