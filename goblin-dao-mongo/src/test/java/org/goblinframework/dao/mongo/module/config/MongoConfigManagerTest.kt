package org.goblinframework.dao.mongo.module.config

import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class MongoConfigManagerTest : SpringContainerObject() {

  @Test
  fun getMongoConfig() {
    val configManager = MongoConfigManager.INSTANCE
    val config = configManager.getMongoConfig("_ut")
    assertNotNull(config)
  }
}