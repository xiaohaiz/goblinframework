package org.goblinframework.cache.redis.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RedisConfigManagerTest {

  @Test
  fun redisConfig() {
    val config = RedisConfigManager.INSTANCE.getRedisConfig("goblin")
    assertNotNull(config)
  }
}