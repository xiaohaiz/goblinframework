package org.goblinframework.database.mysql.client

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class MysqlClientManagerTest {

  @Test
  fun client() {
    val client = MysqlClientManager.INSTANCE.getMysqlClient("_ut")
    assertNotNull(client)
  }
}