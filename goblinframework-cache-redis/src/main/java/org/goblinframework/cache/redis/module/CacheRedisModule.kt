package org.goblinframework.cache.redis.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.redis.cache.RedisCacheBuilder
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.core.system.GoblinSubModule
import org.goblinframework.core.system.ISubModule
import org.goblinframework.core.system.ModuleFinalizeContext
import org.goblinframework.core.system.ModuleInitializeContext

@Install
class CacheRedisModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_REDIS
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RedisConfigManager.INSTANCE.initialize()
    RedisClientManager.INSTANCE.initialize()
    RedisCacheBuilder.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RedisCacheBuilder.INSTANCE.dispose()
    RedisClientManager.INSTANCE.dispose()
    RedisConfigManager.INSTANCE.dispose()
  }
}