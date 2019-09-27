package org.goblinframework.cache.redis.module

import org.goblinframework.api.core.Install
import org.goblinframework.cache.core.module.CacheModule
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.cache.redis.provider.RedisCacheBuilder
import org.goblinframework.core.system.*

@Install
class CacheRedisModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_REDIS
  }

  override fun install(ctx: ModuleInstallContext) {
    val parent = ctx.getExtension(CacheModule::class.java)
    parent?.registerCacheBuilder(RedisCacheBuilder.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RedisConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RedisClientManager.INSTANCE.dispose()
    RedisConfigManager.INSTANCE.dispose()
  }
}