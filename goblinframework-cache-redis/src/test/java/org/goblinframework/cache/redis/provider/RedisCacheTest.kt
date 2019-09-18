package org.goblinframework.cache.redis.provider

import org.bson.types.ObjectId
import org.goblinframework.cache.redis.module.test.FlushRedisCache
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
@FlushRedisCache
class RedisCacheTest {

  @Test
  fun add() {
    val cache = RedisCacheBuilder.INSTANCE.getCache("goblin")!!
    val key = RandomUtils.nextObjectId()
    val value = ObjectId()
    val ret = cache.add(key, 1800, value)
    assertTrue(ret!!)
    val gr = cache.get<ObjectId>(key)!!
    assertEquals(value, gr.value)
    assertTrue(gr.hit)
    assertFalse(gr.wrapper)
  }

  @Test
  fun append() {
    val cache = RedisCacheBuilder.INSTANCE.getCache("goblin")!!
    val key = RandomUtils.nextObjectId()
    cache.set(key, 1800, "HELLO")
    cache.append(key, " WORLD")
  }
}