package org.goblinframework.cache.redis.module

import org.goblinframework.api.annotation.Install
import org.goblinframework.core.bootstrap.GoblinChildModule

@Install
class CacheRedisModule : GoblinChildModule {

  override fun name(): String {
    return "CACHE:REDIS"
  }
}