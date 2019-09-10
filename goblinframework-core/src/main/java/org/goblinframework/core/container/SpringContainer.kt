package org.goblinframework.core.container

import org.goblinframework.core.mbean.GoblinManagedBean
import org.goblinframework.core.mbean.GoblinManagedObject
import org.springframework.context.ApplicationContext

@GoblinManagedBean("CORE")
class SpringContainer(val applicationContext: ApplicationContext) : GoblinManagedObject(), SpringContainerMXBean {

  override fun getUniqueId(): String {
    return (applicationContext as? SpringContainerId)?.uniqueId() ?: throw UnsupportedOperationException()
  }

  internal fun close() {
    unregisterIfNecessary()
  }
}