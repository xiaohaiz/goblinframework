package org.goblinframework.core.event

import org.goblinframework.api.common.Ordered
import org.goblinframework.api.event.GoblinEventContext
import org.goblinframework.api.event.GoblinEventListener
import org.goblinframework.api.service.GoblinManagedBean
import org.goblinframework.api.service.GoblinManagedObject
import org.goblinframework.core.util.ObjectUtils

@GoblinManagedBean(type = "Core", name = "GoblinEventListener")
class GoblinEventListenerImpl
internal constructor(private val delegator: GoblinEventListener)
  : GoblinManagedObject(), GoblinEventListener, Ordered, GoblinEventListenerMXBean {

  override fun getOrder(): Int {
    return ObjectUtils.calculateOrder(delegator)
  }

  override fun accept(context: GoblinEventContext): Boolean {
    return delegator.accept(context)
  }

  override fun onEvent(context: GoblinEventContext) {
    delegator.onEvent(context)
  }
}