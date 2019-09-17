package org.goblinframework.cache.redis.module.config

import org.goblinframework.cache.redis.module.RedisServerMode
import java.io.Serializable

class RedisConfigMapper : Serializable {

  var name: String? = null
  var servers: String? = null
  var password: String? = null
  var mode: RedisServerMode? = null

  companion object {
    private const val serialVersionUID = -3756108596956143695L
  }
}
