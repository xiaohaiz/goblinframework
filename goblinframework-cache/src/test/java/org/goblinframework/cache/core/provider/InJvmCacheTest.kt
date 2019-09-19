package org.goblinframework.cache.core.provider

import org.bson.types.ObjectId
import org.goblinframework.cache.core.cache.CacheSystem
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class InJvmCacheTest {

  @Test
  fun get() {
    val cache = CacheSystem.JVM.defaultCache()!!
    val key = RandomUtils.nextObjectId()
    cache.add(key, 5, ObjectId())

    val gr = cache.get<ObjectId>(key)
    assertTrue(gr.hit)
    assertNotNull(gr.value)
  }
}