package org.goblinframework.database.mongo.persistence

import org.bson.types.ObjectId
import org.goblinframework.api.database.Collection
import org.goblinframework.api.database.Database
import org.goblinframework.api.database.Id
import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.database.core.GoblinDatabaseConnection
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.stereotype.Repository
import org.springframework.test.context.ContextConfiguration
import javax.inject.Inject

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
class GoblinStaticDaoTest : SpringContainerObject() {

  @Database("test")
  @Collection("ut_mock_data")
  class MockData {
    @Id(Id.Generator.OBJECT_ID)
    var id: ObjectId? = null
  }

  @Repository
  @GoblinDatabaseConnection("_ut")
  class MockDataDao : GoblinStaticDao<MockData, ObjectId>()

  @Inject private lateinit var dao: MockDataDao

  @Test
  fun dao() {
  }
}