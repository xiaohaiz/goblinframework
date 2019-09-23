package org.goblinframework.cache.redis.provider

import org.goblinframework.api.cache.GoblinCacheSystem
import org.goblinframework.cache.core.cache.GoblinCacheBuilderManager
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
    val builder = GoblinCacheBuilderManager.INSTANCE.getCacheBuilder(GoblinCacheSystem.RDS)!!
    val cache = builder.getCache("_ut")
    assertNotNull(cache)
  }
}