package org.goblinframework.registry.core.manager

import org.goblinframework.api.common.Singleton
import org.goblinframework.api.registry.IRegistryBuilderManager
import org.goblinframework.api.registry.RegistryBuilder
import org.goblinframework.api.registry.RegistrySystem
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@Singleton
@GoblinManagedBean(type = "registry")
class RegistryBuilderManager private constructor()
  : GoblinManagedObject(), IRegistryBuilderManager, RegistryBuilderManagerMXBean {

  companion object {
    @JvmField val INSTANCE = RegistryBuilderManager()
  }

  fun register(system: RegistrySystem, builder: RegistryBuilder) {}
}