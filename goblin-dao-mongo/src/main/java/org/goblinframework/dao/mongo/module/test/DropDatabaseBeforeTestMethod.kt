package org.goblinframework.dao.mongo.module.test

import com.mongodb.reactivestreams.client.Success
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.reactor.BlockingListSubscriber
import org.goblinframework.core.reactor.BlockingMonoSubscriber
import org.goblinframework.dao.exception.GoblinDaoException
import org.goblinframework.dao.mongo.client.MongoClientManager
import org.slf4j.LoggerFactory

@Singleton
class DropDatabaseBeforeTestMethod private constructor() : TestExecutionListener {

  companion object {
    private val logger = LoggerFactory.getLogger(DropDatabaseBeforeTestMethod::class.java)
    @JvmField val INSTANCE = DropDatabaseBeforeTestMethod()
  }

  override fun beforeTestMethod(testContext: TestContext) {
    val annotations = lookupAnnotations(testContext) ?: return
    val names = annotations.map { it.connection }.distinct().sorted().toList()
    for (name in names) {
      val client = MongoClientManager.INSTANCE.getMongoClient(name)
          ?: throw GoblinDaoException("MongoClient [$name] not found")
      val subscriber = BlockingListSubscriber<String>()
      client.getNativeClient().listDatabaseNames().subscribe(subscriber)
      val databases = subscriber.block().filter { it.startsWith("goblin--ut--") }.toList()
      subscriber.dispose()
      for (database in databases) {
        val db = client.getNativeClient().getDatabase(database)
        val s = BlockingMonoSubscriber<Success>()
        db.drop().subscribe(s)
        s.block()
        s.dispose()
        logger.debug("Database [$database@$name] dropped")
      }
    }
  }

  private fun lookupAnnotations(testContext: TestContext): List<DropDatabase>? {
    val annotations = mutableListOf<DropDatabase>()
    var s = testContext.getTestMethod().getAnnotation(DropDatabase::class.java)
    s?.run { annotations.add(this) }
    var m = testContext.getTestMethod().getAnnotation(DropDatabases::class.java)
    m?.run { annotations.addAll(this.value) }
    s = testContext.getTestClass().getAnnotation(DropDatabase::class.java)
    s?.run { annotations.add(this) }
    m = testContext.getTestClass().getAnnotation(DropDatabases::class.java)
    m?.run { annotations.addAll(this.value) }
    return if (annotations.isEmpty()) null else annotations
  }
}
