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
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import org.springframework.stereotype.Repository
import org.springframework.test.context.ContextConfiguration
import java.util.*
import java.util.concurrent.CountDownLatch
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


    val ids = Collections.synchronizedList(mutableListOf<ObjectId>())
    var latch = CountDownLatch(1)
    publisher.subscribe(object : Subscriber<MockData?> {
      override fun onComplete() {
        latch.countDown()
      }

      override fun onSubscribe(s: Subscription?) {
        s?.request(Long.MAX_VALUE)
      }

      override fun onNext(t: MockData?) {
        t?.run { ids.add(t.id!!) }
      }

      override fun onError(t: Throwable?) {
        t?.printStackTrace()
        latch.countDown()
      }
    })
    latch.await()

    latch = CountDownLatch(1)
    dao.__loads(ids).subscribe(object : Subscriber<MockData?> {
      override fun onComplete() {
        println("finished")
        latch.countDown()
      }

      override fun onSubscribe(s: Subscription?) {
        s?.request(Long.MAX_VALUE)
      }

      override fun onNext(t: MockData?) {
        println("")
      }

      override fun onError(t: Throwable?) {
        t?.printStackTrace()
        latch.countDown()
      }
    })
    latch.await()
  }
}