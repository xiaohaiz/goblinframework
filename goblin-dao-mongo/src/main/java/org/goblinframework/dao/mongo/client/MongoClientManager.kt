package org.goblinframework.dao.mongo.client

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.dao.mongo.module.config.MongoConfigManager
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@Singleton
@ThreadSafe
@GoblinManagedBean("DatabaseMongo")
class MongoClientManager private constructor() : GoblinManagedObject(), MongoClientManagerMXBean {

  companion object {
    @JvmField val INSTANCE = MongoClientManager()
  }

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<MongoClient?>>()

  fun getMongoClient(name: String): MongoClient? {
    buffer[name]?.run { return value }
    lock.withLock {
      buffer[name]?.run { return value }
      val cm = MongoConfigManager.INSTANCE
      val client = cm.getMongoClient(name)?.run { MongoClient(this) }
      buffer[name] = MutableObject(client)
      return client
    }
  }

  override fun disposeBean() {
    lock.withLock {
      buffer.values.mapNotNull { it.value }.forEach { it.dispose() }
      buffer.clear()
    }
  }
}