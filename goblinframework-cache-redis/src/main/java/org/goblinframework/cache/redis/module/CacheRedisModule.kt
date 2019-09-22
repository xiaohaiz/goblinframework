package org.goblinframework.cache.redis.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.cache.redis.module.test.FlushRedisCacheBeforeTestMethod
import org.goblinframework.cache.redis.provider.RedisCacheBuilder
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext
import org.goblinframework.core.bootstrap.GoblinModuleInitializeContext
import org.goblinframework.core.bootstrap.GoblinModuleInstallContext
import org.goblinframework.core.cache.GoblinCacheSystem

@Install
class CacheRedisModule : GoblinChildModule {

  override fun name(): String {
    return "CACHE:REDIS"
  }

  override fun install(ctx: GoblinModuleInstallContext) {
    ctx.registerGoblinCacheBuilder(GoblinCacheSystem.RDS, RedisCacheBuilder.INSTANCE)
    ctx.registerTestExecutionListener(FlushRedisCacheBeforeTestMethod.INSTANCE)
  }

  override fun initialize(ctx: GoblinModuleInitializeContext) {
    RedisConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    RedisClientManager.INSTANCE.dispose()
    RedisConfigManager.INSTANCE.dispose()
  }
}