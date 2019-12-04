package org.goblinframework.cache.couchbase.provider

import org.bson.types.ObjectId
import org.goblinframework.cache.core.annotation.FlushCache
import org.goblinframework.cache.core.cache.CacheSystem
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
@FlushCache(system = CacheSystem.CBS, name = "_ut")
class CouchbaseCacheTest {

  @Test
  fun add() {
    val cache = CacheSystem.CBS.cache("_ut")!!
    val key = RandomUtils.nextObjectId()
    val value = ObjectId()
    val ret = cache.add(key, 1800, value)
    assertTrue(ret)
    val gr = cache.get<ObjectId>(key)
    assertEquals(value, gr.value)
    assertTrue(gr.hit)
    assertFalse(gr.wrapper)
  }
}