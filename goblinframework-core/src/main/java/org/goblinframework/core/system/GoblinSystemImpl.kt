package org.goblinframework.core.system

import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject

@GoblinManagedBean(type = "core", name = "GoblinSystem")
class GoblinSystemImpl internal constructor()
  : GoblinManagedObject(), GoblinSystemMXBean {
}