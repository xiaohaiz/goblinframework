package org.goblinframework.cache.core.api

interface CacheSystemLocationProvider {

  fun getCacheSystemLocation(): CacheSystemLocation

  fun getCacheSystem(): CacheSystem {
    return getCacheSystemLocation().system
  }

  fun getName(): String {
    return getCacheSystemLocation().name
  }
}
