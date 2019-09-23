package org.goblinframework.cache.redis.module

import org.goblinframework.api.common.Install
import org.goblinframework.api.cache.CacheSystem
import org.goblinframework.api.system.*
import org.goblinframework.cache.core.cache.CacheBuilderManager
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.cache.redis.provider.RedisCacheBuilder

@Install
class CacheRedisModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_REDIS
  }

  override fun install(ctx: ModuleInstallContext) {
    CacheBuilderManager.INSTANCE.register(CacheSystem.RDS, RedisCacheBuilder.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RedisConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RedisClientManager.INSTANCE.dispose()
    RedisConfigManager.INSTANCE.dispose()
  }
}