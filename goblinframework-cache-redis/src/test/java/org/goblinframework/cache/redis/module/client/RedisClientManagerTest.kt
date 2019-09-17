package org.goblinframework.cache.redis.module.client

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class RedisClientManagerTest {

  @Test
  fun getRedisClient() {
    val client = RedisClientManager.INSTANCE.getRedisClient("goblin")
    assertNotNull(client)
    RedisClientManager.INSTANCE.closeRedisClient("goblin")
  }
}