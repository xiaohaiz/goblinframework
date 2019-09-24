package org.goblinframework.registry.core.manager

import org.apache.commons.lang3.mutable.MutableObject
import org.goblinframework.api.common.ThreadSafe
import org.goblinframework.api.registry.Registry
import org.goblinframework.api.registry.RegistryBuilder
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

@ThreadSafe
@GoblinManagedBean(type = "registry")
internal class ManagedRegistryBuilder
internal constructor(private val delegator: RegistryBuilder)
  : GoblinManagedObject(), RegistryBuilder, RegistryBuilderMXBean {

  private val lock = ReentrantLock()
  private val buffer = ConcurrentHashMap<String, MutableObject<ManagedRegistry?>>()

  override fun getRegistry(name: String): Registry? {
    buffer[name]?.run { return value }
    return lock.withLock {
      buffer[name]?.run { return value }
      val registry = delegator.getRegistry(name)?.let { ManagedRegistry(it) }
      buffer[name] = MutableObject(registry)
      registry
    }
  }
}