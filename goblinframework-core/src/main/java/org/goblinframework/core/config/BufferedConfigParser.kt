package org.goblinframework.core.config

import org.goblinframework.api.annotation.ThreadSafe
import org.goblinframework.core.exception.GoblinDuplicateException
import org.goblinframework.core.mapper.JsonMapper
import java.util.concurrent.locks.ReentrantReadWriteLock
import kotlin.concurrent.read
import kotlin.concurrent.write

@ThreadSafe
abstract class BufferedConfigParser<E : Config> {

  private val lock = ReentrantReadWriteLock()
  private val buffer = mutableMapOf<String, E>()

  fun asList(): List<E> {
    return lock.read { buffer.values.toList() }
  }

  fun putIntoBuffer(name: String, config: E) {
    lock.write {
      buffer[name]?.run {
        throw GoblinDuplicateException("Duplicated [name=$name,config=$config]")
      }
      doProcessConfig(config)
      buffer[name] = config
    }
  }

  fun getFromBuffer(name: String): E? {
    return lock.read { buffer[name] }
  }

  fun destroy() {
    lock.write {
      buffer.values.forEach { it.destroy() }
      buffer.clear()
    }
  }

  abstract fun initialize()

  open fun doProcessConfig(config: E) {}

  fun <T> parseToMap(mapping: ConfigMapping,
                     name: String,
                     elementType: Class<T>): MutableMap<String, T> {
    val node = mapping[name]
    if (node !is Map<*, *>) {
      return mutableMapOf()
    }
    if (node.isEmpty()) {
      return mutableMapOf()
    }
    val parsed = mutableMapOf<String, T>()
    node.forEach { (k, u) ->
      val n = k!!.toString()
      if (u is Map<*, *>) {
        val mapper = JsonMapper.getDefaultObjectMapper()
        mapper.convertValue(u, elementType)?.run {
          parsed.put(n, this)
        }
      }
    }
    return parsed
  }
}