package org.goblinframework.cache.redis.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.system.*
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.cache.redis.module.test.FlushRedisCacheBeforeTestMethod

@Install
class CacheRedisModule : ISubModule {

  override fun id(): GoblinSubModule {
    return GoblinSubModule.CACHE_REDIS
  }

  override fun install(ctx: ModuleInstallContext) {
    //ctx.registerGoblinCacheBuilder(GoblinCacheSystem.RDS, RedisCacheBuilder.INSTANCE)
    ctx.registerTestExecutionListener(FlushRedisCacheBeforeTestMethod.INSTANCE)
  }

  override fun initialize(ctx: ModuleInitializeContext) {
    RedisConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: ModuleFinalizeContext) {
    RedisClientManager.INSTANCE.dispose()
    RedisConfigManager.INSTANCE.dispose()
  }
}