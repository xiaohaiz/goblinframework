package org.goblinframework.embedded.server

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.api.function.Disposable
import java.util.*
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@Singleton
@ThreadSafe
class EmbeddedServerFactoryManager private constructor() : Disposable {

  companion object {
    @JvmField val INSTANCE = EmbeddedServerFactoryManager()
  }

  private val lock = ReentrantReadWriteLock()
  private val buffer = EnumMap<EmbeddedServerMode, EmbeddedServerFactory>(EmbeddedServerMode::class.java)

  fun register(factory: EmbeddedServerFactory) {
    lock.write {
      buffer[factory.mode()]?.run {
        throw UnsupportedOperationException("Duplicated EmbeddedServerFactory not allowed")
      }
      buffer[factory.mode()] = factory
    }
  }

  fun getFactory(mode: EmbeddedServerMode): EmbeddedServerFactory? {
    return lock.read { buffer[mode] }
  }

  override fun dispose() {
    lock.write {
      buffer.values.filterIsInstance(Disposable::class.java).forEach { it.dispose() }
      buffer.clear()
    }
  }
}