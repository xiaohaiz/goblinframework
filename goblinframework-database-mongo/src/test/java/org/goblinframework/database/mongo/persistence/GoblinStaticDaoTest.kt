package org.goblinframework.database.mongo.persistence

import org.bson.types.ObjectId
import org.goblinframework.api.database.Collection
import org.goblinframework.api.database.Database
import org.goblinframework.api.database.Id
import org.goblinframework.core.container.SpringContainerObject
import org.goblinframework.core.reactor.BlockingListSubscriber
import org.goblinframework.database.core.GoblinDatabaseConnection
import org.goblinframework.test.runner.GoblinTestRunner
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
  fun loads() {
    val id1 = ObjectId()
    val id2 = ObjectId()
    val id3 = ObjectId()
    dao.insert(MockData().apply { id = id1 })
    dao.insert(MockData().apply { id = id2 })
    dao.insert(MockData().apply { id = id3 })

    val ids = listOf(id1, id2, id3)
    val map = dao.loads(ids)
    assertEquals(3, map.size)
    assertTrue(map.containsKey(id1))
    assertTrue(map.containsKey(id2))
    assertTrue(map.containsKey(id3))
  }

  @Test
  fun dao() {
    println(dao.getDatabaseName())
    println(dao.getDatabase())
    println(dao.getCollectionName())
    println(dao.getCollection())

    val dataList = listOf(
        MockData().apply { id = ObjectId() },
        MockData().apply { id = ObjectId() },
        MockData().apply { id = ObjectId() }
    )
    val publisher = dao.__inserts(dataList)


    val s1 = BlockingListSubscriber<MockData>()
    publisher.subscribe(s1)
    val ids = s1.block().map { it.id }.toList()

    val s2 = BlockingListSubscriber<MockData>()
    dao.__loads(ids).subscribe(s2)
    println(s2.block())
  }
}