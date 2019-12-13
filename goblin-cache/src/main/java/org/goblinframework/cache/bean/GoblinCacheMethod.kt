package org.goblinframework.cache.bean

class GoblinCacheMethod internal constructor() {

  internal var type: Class<*>? = null
  internal var parameterList: List<GoblinCacheMethodParameter>? = null
  internal var annotationCount = 0
  internal var multipleCount = 0
  internal var multipleIndex = -1
  internal var parameterCount = 0

}