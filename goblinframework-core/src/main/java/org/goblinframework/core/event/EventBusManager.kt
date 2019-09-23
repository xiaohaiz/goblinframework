package org.goblinframework.core.event

import org.goblinframework.api.annotation.Install
import org.goblinframework.api.common.Ordered
import org.goblinframework.api.event.IEventBusManager

@Install
class EventBusManager : IEventBusManager, Ordered {

  override fun getOrder(): Int {
    return Ordered.HIGHEST_PRECEDENCE
  }
}