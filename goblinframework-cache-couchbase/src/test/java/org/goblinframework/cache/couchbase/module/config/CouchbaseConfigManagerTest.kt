package org.goblinframework.cache.couchbase.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class CouchbaseConfigManagerTest {

  @Test
  fun getCouchbaseConfig() {
    val config = CouchbaseConfigManager.INSTANCE.getCouchbaseConfig("_ut")
    assertNotNull(config)
  }
}