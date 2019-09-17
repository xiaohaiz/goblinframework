package org.goblinframework.core.config

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.exception.GoblinDuplicateException
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@ThreadSafe
abstract class BufferedConfigParser<E : Config> {

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, E>()

  fun putIntoBuffer(name: String, config: E) {
    lock.write {
      buffer[name]?.run {
        throw GoblinDuplicateException("Duplicated [name=$name,config=$config]")
      }
      buffer[name] = config
    }
  }

  fun getFromBuffer(name: String): E? {
    return lock.read { buffer[name] }
  }

  abstract fun initialize()
}