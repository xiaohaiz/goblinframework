package org.goblinframework.registry.core.manager

import org.goblinframework.api.common.Install
import org.goblinframework.api.common.Singleton
import org.goblinframework.api.registry.GoblinRegistryException
import org.goblinframework.api.registry.IRegistryBuilderManager
import org.goblinframework.api.registry.RegistryBuilder
import org.goblinframework.api.registry.RegistrySystem
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import java.util.concurrent.ConcurrentHashMap

@Singleton
@GoblinManagedBean(type = "registry")
class RegistryBuilderManager private constructor()
  : GoblinManagedObject(), IRegistryBuilderManager, RegistryBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RegistryBuilderManager()
  }

  private val buffer = ConcurrentHashMap<RegistrySystem, ManagedRegistryBuilder>()

  override fun getRegistryBuilder(system: RegistrySystem): RegistryBuilder? {
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

  @Install
  class Installer : IRegistryBuilderManager by INSTANCE
}