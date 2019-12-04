package org.goblinframework.cache.redis.provider

import org.goblinframework.cache.core.CacheSystem
import org.goblinframework.cache.core.cache.CacheBuilderManager
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
    val builder = CacheBuilderManager.INSTANCE.getCacheBuilder(CacheSystem.RDS)!!
    val cache = builder.getCache("_ut")
    assertNotNull(cache)
  }
}