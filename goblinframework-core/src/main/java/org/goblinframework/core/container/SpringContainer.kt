package org.goblinframework.core.container

import org.goblinframework.core.service.GoblinManagedBean
import org.goblinframework.core.service.GoblinManagedObject
import org.springframework.context.ApplicationContext

@GoblinManagedBean(type = "core")
class SpringContainer(val applicationContext: ApplicationContext)
  : GoblinManagedObject(), SpringContainerMXBean {

  override fun getUniqueId(): String {
    return (applicationContext as? SpringContainerId)?.uniqueId() ?: throw UnsupportedOperationException()
  }
}