package org.goblinframework.database.mongo.module.test

import com.mongodb.reactivestreams.client.Success
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.test.TestContext
import org.goblinframework.api.test.TestExecutionListener
import org.goblinframework.core.reactor.BlockingListSubscriber
import org.goblinframework.core.reactor.BlockingMonoSubscriber
import org.goblinframework.database.core.GoblinDatabaseException
import org.goblinframework.database.mongo.client.MongoClientManager
import org.slf4j.LoggerFactory

@Singleton
class DropMongoDatabaseBeforeTestMethod private constructor() : TestExecutionListener {

  companion object {
    private val logger = LoggerFactory.getLogger(DropMongoDatabaseBeforeTestMethod::class.java)
    @JvmField val INSTANCE = DropMongoDatabaseBeforeTestMethod()
  }

  override fun beforeTestMethod(testContext: TestContext) {
    val annotations = lookupAnnotations(testContext) ?: return
    val names = annotations.map { it.value }.distinct().sorted().toList()
    for (name in names) {
      val client = MongoClientManager.INSTANCE.getMongoClient(name)
          ?: throw GoblinDatabaseException("MongoClient [$name] not found")
      val subscriber = BlockingListSubscriber<String>()
      client.getNativeClient().listDatabaseNames().subscribe(subscriber)
      val databases = subscriber.block().filter { it.startsWith("goblin-test-") }.toList()
      for (database in databases) {
        val db = client.getNativeClient().getDatabase(database)
        val s = BlockingMonoSubscriber<Success>()
        db.drop().subscribe(s)
        s.block()
        logger.debug("Database [$database@$name] dropped")
      }
    }
  }

  private fun lookupAnnotations(testContext: TestContext): List<DropMongoDatabase>? {
    val annotations = mutableListOf<DropMongoDatabase>()
    var s = testContext.getTestMethod().getAnnotation(DropMongoDatabase::class.java)
    s?.run { annotations.add(this) }
    var m = testContext.getTestMethod().getAnnotation(DropMongoDatabases::class.java)
    m?.run { annotations.addAll(this.value) }
    s = testContext.getTestClass().getAnnotation(DropMongoDatabase::class.java)
    s?.run { annotations.add(this) }
    m = testContext.getTestClass().getAnnotation(DropMongoDatabases::class.java)
    m?.run { annotations.addAll(this.value) }
    return if (annotations.isEmpty()) null else annotations
  }
}
