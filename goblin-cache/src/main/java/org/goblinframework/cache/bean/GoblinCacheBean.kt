package org.goblinframework.cache.bean

import java.lang.reflect.Method

class GoblinCacheBean internal constructor(private val caches: Map<Class<*>, GoblinCache>,
                                           private val methods: Map<Method, GoblinCacheMethod>) {

  fun isEmpty(): Boolean {
    return caches.isEmpty()
  }

  fun getGoblinCache(type: Class<*>): GoblinCache? {
    return caches[type]
  }

  fun getGoblinCacheMethod(method: Method): GoblinCacheMethod? {
    return methods[method]
  }
}