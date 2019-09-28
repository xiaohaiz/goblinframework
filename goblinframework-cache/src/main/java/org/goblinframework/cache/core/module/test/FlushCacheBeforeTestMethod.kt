package org.goblinframework.cache.core.module.test

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.cache.core.annotation.FlushCache
import org.goblinframework.cache.core.annotation.FlushCaches
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
      val name = it.name.trim()
      caches = caches.filter { c -> c.cacheName() == name }.toList()
      caches.forEach { c -> c.flush() }
    }
  }

  private fun extractAnnotations(testContext: TestContext): List<FlushCache>? {
    val annotations = mutableListOf<FlushCache>()
    testContext.getTestMethod().getAnnotation(FlushCache::class.java)?.run {
      annotations.add(this)
    }
    testContext.getTestMethod().getAnnotation(FlushCaches::class.java)?.run {
      annotations.addAll(this.value)
    }
    testContext.getTestClass().getAnnotation(FlushCache::class.java)?.run {
      annotations.add(this)
    }
    testContext.getTestClass().getAnnotation(FlushCaches::class.java)?.run {
      annotations.addAll(this.value)
    }
    return if (annotations.isEmpty()) null else annotations
  }
}