package org.goblinframework.core.system

import org.goblinframework.api.spi.ExtModuleContext
import java.util.concurrent.ConcurrentHashMap

abstract class ExtModuleContextImpl : ExtModuleContext {

  private val extensions = ConcurrentHashMap<String, Any>()

  override fun <E : Any> setExtension(name: String, value: E) {
    extensions[name] = value
  }

  @Suppress("UNCHECKED_CAST")
  override fun <E : Any?> getExtension(name: String): E? {
    return extensions[name] as E
  }

  override fun <E : Any?> getExtension(type: Class<E>): E? {
    return getExtension(type.name)
  }

  @Suppress("UNCHECKED_CAST")
  override fun <E : Any?> removeExtension(name: String): E? {
    return extensions.remove(name) as E
  }
}