package org.goblinframework.cache.redis.provider

import org.bson.types.ObjectId
import org.goblinframework.cache.redis.module.test.FlushRedisCache
import org.goblinframework.core.util.RandomUtils
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration
import kotlin.math.abs

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

  @Test
  fun incr() {
    val cache = RedisCacheBuilder.INSTANCE.getCache("goblin")!!
    // no initial value
    var key = RandomUtils.nextObjectId()
    var value = cache.incr(key, 10, 100, 3600)
    assertEquals(100, value!!.toLong())
    value = cache.incr(key, 10, 200, 7200) // initial and expiration skipped
    assertEquals(110, value!!.toLong())
    var ttl = cache.ttl(key)
    assertTrue(abs(ttl!!.toLong() - 3600) < 10)

    // initial value present
    key = RandomUtils.nextObjectId()
    cache.set(key, 1800, "50")
    value = cache.incr(key, 10, 200, 3600) // initial and expiration skipped
    assertEquals(60, value!!.toLong())
    value = cache.incr(key, 10, 200, 3600) // initial and expiration skipped
    assertEquals(70, value!!.toLong())
    ttl = cache.ttl(key)
    assertTrue(abs(ttl!!.toLong() - 1800) < 10)
  }

  @Test
  fun decr() {
    val cache = RedisCacheBuilder.INSTANCE.getCache("goblin")!!
    // no initial value
    var key = RandomUtils.nextObjectId()
    var value = cache.decr(key, 10, 100, 3600)
    assertEquals(100, value!!.toLong())
    value = cache.decr(key, 10, 200, 7200) // initial and expiration skipped
    assertEquals(90, value!!.toLong())
    var ttl = cache.ttl(key)
    assertTrue(abs(ttl!!.toLong() - 3600) < 10)

    // initial value present
    key = RandomUtils.nextObjectId()
    cache.set(key, 1800, "50")
    value = cache.decr(key, 10, 200, 3600) // initial and expiration skipped
    assertEquals(40, value!!.toLong())
    value = cache.decr(key, 10, 200, 3600) // initial and expiration skipped
    assertEquals(30, value!!.toLong())
    ttl = cache.ttl(key)
    assertTrue(abs(ttl!!.toLong() - 1800) < 10)
  }
}