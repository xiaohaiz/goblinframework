package org.goblinframework.cache.redis.provider

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RedisCacheBuilderTest {

  @Test
  fun getCache() {
    val cache = RedisCacheBuilder.INSTANCE.getCache("goblin")
    assertNotNull(cache)
  }
}