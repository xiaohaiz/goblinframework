package org.goblinframework.dao.mongo.client

import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.test.context.ContextConfiguration

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class MongoClientManagerTest : SpringContainerObject() {

  @Test
  fun getMongoClient() {
    val clientManager = MongoClientManager.INSTANCE
    val client = clientManager.getMongoClient("_ut")
    assertNotNull(client)
  }
}