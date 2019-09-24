package org.goblinframework.registry.core.manager

import org.goblinframework.api.common.Disposable
import org.goblinframework.api.registry.Registry
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "registry")
internal class ManagedRegistry
internal constructor(private val delegator: Registry)
  : GoblinManagedObject(), Registry by delegator, RegistryMXBean {

  override fun disposeBean() {
    (delegator as? Disposable)?.dispose()
  }
}