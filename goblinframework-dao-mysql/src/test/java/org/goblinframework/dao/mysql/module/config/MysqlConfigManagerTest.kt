package org.goblinframework.dao.mysql.module.config

import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class MysqlConfigManagerTest {

  @Test
  fun config() {
    val configManager = MysqlConfigManager.INSTANCE
    val config = configManager.getMysqlConfig("ut")
    assertNotNull(config)
  }
}