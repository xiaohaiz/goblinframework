package org.goblinframework.cache.core.provider

import org.bson.types.ObjectId
import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.annotation.FlushCache
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
@FlushCache(system = CacheSystem.JVM, name = "_ut")
class InJvmCacheTest {

  @Test
  fun get() {
    val cache = CacheSystem.JVM.cache("_u")!!
    val key = RandomUtils.nextObjectId()
    cache.add(key, 5, ObjectId())

    val gr = cache.get<ObjectId>(key)
    assertTrue(gr.hit)
    assertNotNull(gr.value)
  }

  @Test
  fun append() {
    val cache = CacheSystem.JVM.cache("_u")!!
    val key = RandomUtils.nextObjectId()
    cache.add(key, 5, "HELLO")
    val ret = cache.append(key, " WORLD")
    assertTrue(ret)
    assertEquals("HELLO WORLD", cache.load<String>(key))
  }
}