package org.goblinframework.cache.redis.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.cache.redis.client.RedisClientManager
import org.goblinframework.cache.redis.module.config.RedisConfigManager
import org.goblinframework.core.bootstrap.GoblinChildModule
import org.goblinframework.core.bootstrap.GoblinModuleBootstrapContext
import org.goblinframework.core.bootstrap.GoblinModuleFinalizeContext

@Install
class CacheRedisModule : GoblinChildModule {

  override fun name(): String {
    return "CACHE:REDIS"
  }

  override fun bootstrap(ctx: GoblinModuleBootstrapContext) {
    RedisConfigManager.INSTANCE.initialize()
  }

  override fun finalize(ctx: GoblinModuleFinalizeContext) {
    RedisClientManager.INSTANCE.destroy()
    RedisConfigManager.INSTANCE.destroy()
  }
}