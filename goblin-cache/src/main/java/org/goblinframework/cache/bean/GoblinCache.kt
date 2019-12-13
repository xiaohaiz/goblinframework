package org.goblinframework.cache.bean

import org.goblinframework.cache.annotation.GoblinCacheExpiration
import org.goblinframework.cache.core.cache.Cache
import org.goblinframework.cache.core.cache.CacheLocation
import org.goblinframework.cache.util.CacheExpirationCalculator

class GoblinCache {

  var type: Class<*>? = null
  var location: CacheLocation? = null
  var wrapper = false
  var expirationPolicy = GoblinCacheExpiration.Policy.FIXED
  var expirationValue = GoblinCacheExpiration.DEFAULT_EXPIRATION
  var cache: Cache? = null

  fun calculateExpiration(): Int {
    val annotation = type!!.getAnnotation(GoblinCacheExpiration::class.java)
    return if (annotation != null && annotation.enable) {
      CacheExpirationCalculator.expirationInSeconds(annotation)
    } else CacheExpirationCalculator.expirationInSeconds(expirationPolicy, expirationValue)
  }

  fun cache(): Cache {
    return cache!!
  }
}