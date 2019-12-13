package org.goblinframework.cache.module.test

import org.goblinframework.api.annotation.Singleton
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
      val connection = it.connection.trim()
      CacheBuilderManager.INSTANCE.getCacheBuilder(system)?.run {
        val cacheBuilder = this
        cacheBuilder.getCache(connection)?.run {
          val cache = this
          cache.flush()
        }
      }
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