package org.goblinframework.core.container

import org.goblinframework.api.container.ApplicationContextProvider
import org.goblinframework.api.core.GoblinManagedBean
import org.goblinframework.api.core.GoblinManagedObject
import org.springframework.context.ApplicationContext

@GoblinManagedBean(type = "core")
class SpringContainer(val applicationContext: ApplicationContext)
  : GoblinManagedObject(), SpringContainerMXBean, ApplicationContextProvider {

  override fun getUniqueId(): String {
    return (applicationContext as? SpringContainerId)?.uniqueId() ?: throw UnsupportedOperationException()
  }

  override fun applicationContext(): Any {
    return applicationContext
  }
}