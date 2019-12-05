package org.goblinframework.cache.couchbase.client

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class CouchbaseEnvironmentManagerTest {

  @Test
  fun getCouchbaseEnvironment() {
    val environment = CouchbaseEnvironmentManager.INSTANCE.getCouchbaseEnvironment()
    assertNotNull(environment)
  }
}