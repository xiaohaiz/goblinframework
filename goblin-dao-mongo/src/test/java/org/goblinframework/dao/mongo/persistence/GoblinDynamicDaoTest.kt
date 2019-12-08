package org.goblinframework.dao.mongo.persistence

import org.goblinframework.api.dao.GoblinId
import org.goblinframework.core.util.StringUtils
import org.goblinframework.dao.core.annotation.PersistenceConnection
import org.goblinframework.dao.mongo.annotation.GoblinCollection
import org.goblinframework.dao.mongo.annotation.GoblinDatabase
import org.goblinframework.database.mongo.module.test.DropMongoDatabase
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.stereotype.Repository
import org.springframework.test.context.ContextConfiguration
import javax.inject.Inject

@RunWith(GoblinTestRunner::class)
@ContextConfiguration("/UT.xml")
@DropMongoDatabase("_ut")
class GoblinDynamicDaoTest {

  class MockDynamicData {
    @GoblinId(GoblinId.Generator.NONE)
    var id: Long? = null
  }

  @Repository
  @PersistenceConnection(connection = "_ut")
  @GoblinDatabase("test")
  @GoblinCollection("ut_mock_dynamic_data_{}", true)
  open class MocDynamicDataDao : GoblinDynamicDao<MockDynamicData, Long>() {

    override fun calculateDatabaseName(template: String, entity: MockDynamicData?): String? {
      return null
    }

    override fun calculateCollectionName(template: String, entity: MockDynamicData): String? {
      val mod = entity.id!! % 10
      return StringUtils.formatMessage(template, mod)
    }
  }

  @Inject private lateinit var dao: MocDynamicDataDao

  @Test
  fun insert() {
    dao.insert(MockDynamicData().also { it.id = 1 })
    dao.insert(MockDynamicData().also { it.id = 2 })
    dao.insert(MockDynamicData().also { it.id = 3 })
    assertNotNull(dao.load(1))
    assertNotNull(dao.load(2))
    assertNotNull(dao.load(3))
  }
}