package org.goblinframework.registry.core.manager

import org.goblinframework.api.annotation.Singleton
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.goblinframework.registry.core.GoblinRegistryException
import org.goblinframework.registry.core.RegistryBuilder
import org.goblinframework.registry.core.RegistrySystem
import java.util.concurrent.ConcurrentHashMap

@Singleton
@GoblinManagedBean(type = "registry")
class RegistryBuilderManager private constructor()
  : GoblinManagedObject(), RegistryBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RegistryBuilderManager()
  }

  private val buffer = ConcurrentHashMap<RegistrySystem, ManagedRegistryBuilder>()

  fun getRegistryBuilder(system: RegistrySystem): RegistryBuilder? {
    return buffer[system]
  }

  @Synchronized
  fun register(system: RegistrySystem, builder: RegistryBuilder) {
    buffer[system]?.run {
      throw GoblinRegistryException("Registry build [$system] already exists")
    }
    buffer[system] = ManagedRegistryBuilder(builder)
  }

  override fun disposeBean() {
    buffer.values.forEach { it.dispose() }
    buffer.clear()
  }

}