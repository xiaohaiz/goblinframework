package org.goblinframework.core.transcoder

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.exception.GoblinDuplicateException
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
class DecodedObjectManager private constructor() {

  companion object {
    @JvmField val INSTANCE = DecodedObjectManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val types = mutableMapOf<String, Class<*>>()

  fun register(id: String, type: Class<*>) {
    lock.write {
      types[id]?.run {
        throw GoblinDuplicateException("DecodedObject $id already registered")
      }
      types[id] = type
    }
  }

  fun typeOf(id: String): Class<*>? {
    return lock.read { types[id] }
  }
}