package org.goblinframework.core.system

import org.goblinframework.api.system.ModuleContext
import org.goblinframework.api.system.SubModules
import java.util.concurrent.ConcurrentHashMap

abstract class ModuleContextImpl : ModuleContext {

  private val extensions = ConcurrentHashMap<String, Any>()

  override fun createSubModules(): SubModules {
    return SubModulesImpl()
  }

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