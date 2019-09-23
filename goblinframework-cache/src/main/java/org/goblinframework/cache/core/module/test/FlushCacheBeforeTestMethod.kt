package org.goblinframework.cache.core.module.test

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.cache.FlushCache
import org.goblinframework.api.cache.FlushCaches
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.cache.core.cache.CacheBuilderManager

@Singleton
class FlushCacheBeforeTestMethod private constructor() : TestExecutionListener {

  companion object {
    @JvmField val INSTANCE = FlushCacheBeforeTestMethod()
  }

  override fun beforeTestMethod(testContext: TestContext) {
    val annotations = extractAnnotations(testContext) ?: return
    annotations.forEach {
      val system = it.system
      var caches = CacheBuilderManager.INSTANCE.asCacheList(system)
      if (!it.name.isBlank()) {
        val name = it.name.trim()
        caches = caches.filter { c -> c.cacheName() == name }.toList()
      } else {
        system.defaultCache()?.run {
          caches = listOf(this)
        }
      }
      caches.forEach { c -> c.flush() }
    }
  }

  private fun extractAnnotations(testContext: TestContext): List<FlushCache>? {
    val annotations = mutableListOf<FlushCache>()
    testContext.testMethod.getAnnotation(FlushCache::class.java)?.run {
      annotations.add(this)
    }
    testContext.testMethod.getAnnotation(FlushCaches::class.java)?.run {
      annotations.addAll(this.value)
    }
    testContext.testClass.getAnnotation(FlushCache::class.java)?.run {
      annotations.add(this)
    }
    testContext.testClass.getAnnotation(FlushCaches::class.java)?.run {
      annotations.addAll(this.value)
    }
    return if (annotations.isEmpty()) null else annotations
  }
}