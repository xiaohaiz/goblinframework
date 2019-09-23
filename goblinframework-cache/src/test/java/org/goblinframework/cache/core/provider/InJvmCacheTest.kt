package org.goblinframework.cache.core.provider

import org.bson.types.ObjectId
import org.goblinframework.api.cache.GoblinCacheSystem
import org.goblinframework.cache.core.cache.GoblinCacheBuilderManager
import org.goblinframework.cache.core.module.test.FlushInJvmCache
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
@FlushInJvmCache
class InJvmCacheTest {

  @Test
  fun get() {
    val builderManager = GoblinCacheBuilderManager.INSTANCE
    val builder = builderManager.getCacheBuilder(GoblinCacheSystem.JVM)
    val cache = builder?.getCache("JVM")!!
    val key = RandomUtils.nextObjectId()
    cache.add(key, 5, ObjectId())

    val gr = cache.get<ObjectId>(key)
    assertTrue(gr.hit)
    assertNotNull(gr.value)
  }
}