package org.goblinframework.registry.core.manager

import org.goblinframework.api.function.Disposable
import org.goblinframework.api.registry.Registry
import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject

@GoblinManagedBean(type = "registry")
internal class ManagedRegistry
internal constructor(private val delegator: Registry)
  : GoblinManagedObject(), Registry by delegator, RegistryMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
  }
}